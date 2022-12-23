import sys
from math import radians, sin, cos

import jax
import jax.numpy as jnp
import numpy as np
from tqdm import tqdm
import argparse
from PIL import Image

if __name__ == '__main__':
    jax.config.update("jax_enable_x64", True)

    parser = argparse.ArgumentParser(description='LBF solver')
    parser.add_argument('-m', '--mask', help='Path to bmp file with buildings mask', required=True)
    parser.add_argument('-z', '--zdim', help='Dimension along z axis (>0)', type=int, required=False, default=80)
    parser.add_argument('-v', '--vel', help='Inflow wind velocity [m/s] (>0)', type=float, required=False, default=2.0)
    parser.add_argument('-d', '--dire', help='Inflow wind direction (90 >= _ >= 0)', type=float, required=False, default=0.0)
    parser.add_argument('-i', '--iter', help='Number of iterations (>0)', type=int, required=True)
    parser.add_argument('-r', '--reyn', help='Reynolds number for simulation (>0)', type=float, required=False, default=50.0)
    parser.add_argument('-s', '--res', help='Path to file to save results', required=True)
    parser.add_argument('-c', '--clen', help='Cell length side', type=int, required=False, default=1)
    parser.add_argument('-n', '--indirect', help='Indirect save steps', type=int, required=False, default=-1)

    parser.add_argument('-xmin', '--xmin', help='x min', type=int, required=False)
    parser.add_argument('-xmax', '--xmax', help='x max', type=int, required=False)
    parser.add_argument('-ymin', '--ymin', help='y min', type=int, required=False)
    parser.add_argument('-ymax', '--ymax', help='y max', type=int, required=False)

    args = vars(parser.parse_args())
    if args['zdim'] <= 0 or args['vel'] <= 0 or args['iter'] <= 0 or args['reyn'] <= 0 or args['dire'] < 0 or args['dire'] >= 360 or args['clen'] <= 0:
        parser.print_help()
        sys.exit()

    nz = int(args['zdim']) + 2
    bitmap_image = np.array(Image.open(args['mask']))
    if bitmap_image.ndim == 3:
        bitmap_mask = bitmap_image[:, :, 0]
    else:
        bitmap_mask = bitmap_image[:, :]
    bitmap_mask = np.full((len(bitmap_mask), len(bitmap_mask[0])), 255) - bitmap_mask

    if args['dire'] >= 0 and args['dire'] <= 90:
        wind_dire = args['dire']
        bitmap_mask_flipped = bitmap_mask
    elif args['dire'] <= 180:
        wind_dire = 180 - args['dire']
        bitmap_mask_flipped = np.fliplr(bitmap_mask)
    elif args['dire'] <= 270:
        wind_dire = args['dire'] - 180
        bitmap_mask_flipped = np.flipud(np.fliplr(bitmap_mask))
    else:
        wind_dire = 360 - args['dire']
        bitmap_mask_flipped = np.flipud(bitmap_mask)

    nx = len(bitmap_mask) + 2
    ny = len(bitmap_mask[0]) + 2

    mask = np.full((nx, ny, nz), False)

    for i in range(nx - 2):
        for j in range(ny - 2):
            if bitmap_mask_flipped[i, j] > 0:
                h = min(nz-1, 1+int(bitmap_mask_flipped[i, j]/float(args['clen'])))
                mask[i + 1, j + 1, :h] = np.full(h, True)

    obstacle_mask = jnp.array(mask)

    print("Created obstacle mask from file {} with dimnesions {}x{}x{}".format(args['mask'], nx - 2, ny - 2, nz - 2))

    INFLOW_VELOCITY = float(args['vel']) * 0.57735 / 331.0

    reynolds_number = float(args['reyn'])
    KINEMATIC_VISCOSITY = (INFLOW_VELOCITY * 5) / reynolds_number
    RELAXATION_OMEGA = (1.0 / (3.0 * KINEMATIC_VISCOSITY + 0.5))
    print("Horizontal inflow velocity {} m/n and reynolds number {}".format(args['vel'], reynolds_number))

    N_ITERATIONS = int(args['iter'])

    x = jnp.arange(nx)
    y = jnp.arange(ny)
    z = jnp.arange(nz)
    X, Y, Z = jnp.meshgrid(x, y, z, indexing="ij")


    def get_density(discrete_velocities):
        density = jnp.sum(discrete_velocities, axis=-1)
        return density


    def get_macroscopic_velocities(discrete_velocities, density):
        return jnp.einsum("NMLQ,dQ->NMLd", discrete_velocities, LATTICE_VELOCITIES) / density[..., jnp.newaxis]


    def get_equilibrium_discrete_velocities(macroscopic_velocities, density):
        projected_discrete_velocities = jnp.einsum("dQ,NMLd->NMLQ", LATTICE_VELOCITIES, macroscopic_velocities)
        macroscopic_velocity_magnitude = jnp.linalg.norm(macroscopic_velocities, axis=-1, ord=2)
        equilibrium_discrete_velocities = (
                    density[..., jnp.newaxis] * LATTICE_WEIGHTS[jnp.newaxis, jnp.newaxis, jnp.newaxis, :] *
                    (1 + 3 * projected_discrete_velocities + 9 / 2 * projected_discrete_velocities ** 2 -
                     3 / 2 * macroscopic_velocity_magnitude[..., jnp.newaxis] ** 2))
        return equilibrium_discrete_velocities


    N_DISCRETE_VELOCITIES = 19

    LATTICE_INDICES = jnp.array([0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18])
    LATICE_VELOCITIES_X = jnp.array([0, 1, 0, -1, 0, 0, 0, 1, -1, -1, 1, 1, -1, -1, 1, 0, 0, 0, 0])
    LATICE_VELOCITIES_Y = jnp.array([0, 0, 1, 0, -1, 0, 0, 1, 1, -1, -1, 0, 0, 0, 0, 1, -1, -1, 1])
    LATICE_VELOCITIES_Z = jnp.array([0, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 1, 1, -1, -1, 1, 1, -1, -1])

    OPPOSITE_LATTICE_INDICES = jnp.array([0, 3, 4, 1, 2, 6, 5, 9, 10, 7, 8, 13, 14, 11, 12, 17, 18, 15, 16])

    LATTICE_VELOCITIES = jnp.array([LATICE_VELOCITIES_X,
                                    LATICE_VELOCITIES_Y,
                                    LATICE_VELOCITIES_Z])

    LATTICE_WEIGHTS = jnp.array([
        1 / 3,
        1 / 18, 1 / 18, 1 / 18, 1 / 18, 1 / 18, 1 / 18,
        1 / 36, 1 / 36, 1 / 36, 1 / 36, 1 / 36, 1 / 36, 1 / 36, 1 / 36, 1 / 36, 1 / 36, 1 / 36, 1 / 36])

    X_POSITIVE_VELOCITIES = jnp.array([1, 7, 10, 11, 14])
    Y_POSITIVE_VELOCITIES = jnp.array([2, 7, 8, 15, 18])
    X_NEGATIVE_VELOCITIES = jnp.array([3, 8, 9, 12, 13])
    Y_NEGATIVE_VELOCITIES = jnp.array([4, 9, 10, 16, 17])
    YZ_VELOCITIES = jnp.array([0, 2, 4, 5, 6, 15, 16, 17, 18])
    XZ_VELOCITIES = jnp.array([0, 1, 3, 5, 6, 11, 12, 13, 14])
    Z_NEGATIVE_VELOCITIES = jnp.array([6, 13, 14, 17, 18])
    Z_POSITIVE_VELOCITIES = jnp.array([5, 12, 11, 16, 15])

    wind_dire_rad = radians(wind_dire)
    x_wind_speed = np.float32(INFLOW_VELOCITY * sin(wind_dire_rad))
    y_wind_speed = np.float32(INFLOW_VELOCITY * cos(wind_dire_rad))
    print("Vertical (x) wind component: {} m/s".format(round(100 * args['vel'] * sin(wind_dire_rad)) / 100))
    print("Horizontal (y) wind component: {} m/s".format(round(100 * args['vel'] * cos(wind_dire_rad)) / 100))

    VELOCITY_PROFILE = jnp.zeros((nx, ny, nz, 3), dtype=np.float32)
    VELOCITY_PROFILE = VELOCITY_PROFILE.at[:, :, :, 0].set(x_wind_speed)
    VELOCITY_PROFILE = VELOCITY_PROFILE.at[:, :, :, 1].set(y_wind_speed)
    discrete_velocities_prev = get_equilibrium_discrete_velocities(VELOCITY_PROFILE,
                                                                   jnp.ones((nx, ny, nz), dtype=np.float32))


    @jax.jit
    def update(discrete_velocities_prev):
        # (1) Prescribe the outflow BC on the right boundary. Flow can go out, but not back in.
        # Ground boundary condition
        discrete_velocities_prev = discrete_velocities_prev.at[:, :, 0, Z_POSITIVE_VELOCITIES].set(
            discrete_velocities_prev[:, :, 0, Z_NEGATIVE_VELOCITIES])
        # Sky boundary condition
        discrete_velocities_prev = discrete_velocities_prev.at[:, :, -1, Z_NEGATIVE_VELOCITIES].set(
            discrete_velocities_prev[:, :, -2, Z_NEGATIVE_VELOCITIES])

        discrete_velocities_prev = discrete_velocities_prev.at[-1, :, :, X_NEGATIVE_VELOCITIES].set(
            discrete_velocities_prev[-2, :, :, X_NEGATIVE_VELOCITIES])
        discrete_velocities_prev = discrete_velocities_prev.at[:, -1, :, Y_NEGATIVE_VELOCITIES].set(
            discrete_velocities_prev[:, -2, :, Y_NEGATIVE_VELOCITIES])
        if wind_dire == 0:
            discrete_velocities_prev = discrete_velocities_prev.at[0, :, :, X_POSITIVE_VELOCITIES].set(
                discrete_velocities_prev[1, :, :, X_POSITIVE_VELOCITIES])
        if wind_dire == 90:
            discrete_velocities_prev = discrete_velocities_prev.at[:, 0, :, Y_POSITIVE_VELOCITIES].set(
                discrete_velocities_prev[:, 1, :, Y_POSITIVE_VELOCITIES])

        # (2) Determine macroscopic velocities
        density_prev = get_density(discrete_velocities_prev)
        macroscopic_velocities_prev = get_macroscopic_velocities(
            discrete_velocities_prev,
            density_prev)

        # (3) Prescribe Inflow Dirichlet BC using Zou/He scheme in 3D:
        if wind_dire != 0:
            macroscopic_velocities_prev = macroscopic_velocities_prev.at[0, :, :, :].set(
                VELOCITY_PROFILE[0, :, :, :])
        if wind_dire != 90:
            macroscopic_velocities_prev = macroscopic_velocities_prev.at[:, 0, :, :].set(
                VELOCITY_PROFILE[:, 0, :, :])

        lateral_YZ_densities = get_density(
            jnp.transpose(discrete_velocities_prev[0, :, :, YZ_VELOCITIES], axes=(1, 2, 0)))
        lateral_XZ_densities = get_density(
            jnp.transpose(discrete_velocities_prev[:, 0, :, XZ_VELOCITIES], axes=(1, 2, 0)))
        left_densities = get_density(
            jnp.transpose(discrete_velocities_prev[0, :, :, X_NEGATIVE_VELOCITIES], axes=(1, 2, 0)))
        south_densities = get_density(
            jnp.transpose(discrete_velocities_prev[:, 0, :, Y_NEGATIVE_VELOCITIES], axes=(1, 2, 0)))

        if wind_dire != 0:
            density_prev = density_prev.at[0, :, :].set((lateral_YZ_densities + 2 * left_densities) /
                                                        (1 - macroscopic_velocities_prev[0, :, :, 0]))
        if wind_dire != 90:
            density_prev = density_prev.at[:, 0, :].set((lateral_XZ_densities + 2 * south_densities) /
                                                        (1 - macroscopic_velocities_prev[:, 0, :, 1]))

        # (4) Compute discrete Equilibria velocities
        equilibrium_discrete_velocities = get_equilibrium_discrete_velocities(
            macroscopic_velocities_prev,
            density_prev)

        # (3) Belongs to the Zou/He scheme
        if wind_dire != 0:
            discrete_velocities_prev = \
                discrete_velocities_prev.at[0, :, :, X_POSITIVE_VELOCITIES].set(
                    equilibrium_discrete_velocities[0, :, :, X_POSITIVE_VELOCITIES])
        if wind_dire != 90:
            discrete_velocities_prev = \
                discrete_velocities_prev.at[:, 0, :, Y_POSITIVE_VELOCITIES].set(
                    equilibrium_discrete_velocities[:, 0, :, Y_POSITIVE_VELOCITIES])

        # (5) Collide according to BGK
        discrete_velocities_post_collision = (discrete_velocities_prev - RELAXATION_OMEGA *
                                              (discrete_velocities_prev - equilibrium_discrete_velocities))

        # (6) Bounce-Back Boundary Conditions to enfore the no-slip
        for i in range(N_DISCRETE_VELOCITIES):
            discrete_velocities_post_collision = discrete_velocities_post_collision.at[
                obstacle_mask, LATTICE_INDICES[i]].set(
                discrete_velocities_prev[obstacle_mask, OPPOSITE_LATTICE_INDICES[i]]
            )

        # (7) Stream alongside lattice velocities
        discrete_velocities_streamed = discrete_velocities_post_collision
        for i in range(N_DISCRETE_VELOCITIES):
            discrete_velocities_streamed = discrete_velocities_streamed.at[:, :, :, i].set(
                jnp.roll(
                    jnp.roll(
                        jnp.roll(
                            discrete_velocities_post_collision[:, :, :, i], LATTICE_VELOCITIES[0, i], axis=0),
                        LATTICE_VELOCITIES[1, i], axis=1),
                    LATTICE_VELOCITIES[2, i], axis=2))

        return discrete_velocities_streamed


    def recalculate_velocity(velocity):
        return velocity * 331.0 / 0.57735


    def save(discrete_velocities, iteration, nx, ny, nz):
        file = open(args['res'] + "-" + str(iteration) + "-velocity", "w")

        x_min = 1
        x_max = nx - 1
        y_min = 1
        y_max = ny - 1
        if args['xmin'] is not None:
            x_min = max(args['xmin']+1, 1)
        if args['xmax'] is not None:
            x_max = min(args['xmax'], nx - 1)
        if args['ymin'] is not None:
            y_min = max(args['ymin']+1, 1)
        if args['ymax'] is not None:
            y_max = min(args['ymax'], ny - 1)
        file.write(str(x_max-x_min) + ';' + str(y_max-y_min) + ';' + str(nz - 2) + '\n')

        density = get_density(discrete_velocities)
        macroscopic_velocities = get_macroscopic_velocities(
            discrete_velocities,
            density)

        if args['dire'] >= 0 and args['dire'] <= 90:
            velocities = np.array(macroscopic_velocities)
            flipped_mask = mask
            y_coef = 1
            x_coef = 1
        elif args['dire'] <= 180:
            velocities = np.flip(np.array(macroscopic_velocities), 1)
            flipped_mask = np.flip(mask, 1)
            y_coef = -1
            x_coef = 1
        elif args['dire'] <= 270:
            velocities = np.flip(np.flip(np.array(macroscopic_velocities), 1), 0)
            flipped_mask = np.flip(np.flip(mask, 1), 0)
            y_coef = -1
            x_coef = -1
        else:
            velocities = np.flip(np.array(macroscopic_velocities), 0)
            flipped_mask = np.flip(mask, 0)
            y_coef = 1
            x_coef = -1

        for i in range(x_min, x_max):
            for j in range(y_min, y_max):
                for k in range(1, nz - 1):
                    if flipped_mask[i, j, k]:
                        file.write("0.0;0.0;0.0 ")
                    else:
                        file.write(str(x_coef * recalculate_velocity(velocities[i, j, k, 0])) + ";" + str(
                            y_coef * recalculate_velocity(velocities[i, j, k, 1])) + ";" + str(
                            recalculate_velocity(velocities[i, j, k, 2])) + " ")
        file.close()


    def run(discrete_velocities):
        print("Calculating wind flow:")
        for iteration in tqdm(range(N_ITERATIONS)):
            discrete_velocities_next = update(discrete_velocities)
            discrete_velocities = discrete_velocities_next
            if args['indirect'] > 0 and (iteration + 1) % args['indirect'] == 0 and iteration + 1 < N_ITERATIONS:
                save(discrete_velocities, iteration + 1, nx, ny, nz)

        return discrete_velocities


    final_discrete_velocities = run(discrete_velocities_prev)
    save(final_discrete_velocities, N_ITERATIONS, nx, ny, nz)