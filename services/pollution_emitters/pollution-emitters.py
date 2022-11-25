import argparse
import sys

import numpy as np
from PIL import Image

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Temperature distribution')
    parser.add_argument('-m', '--mask', help='Path to bmp file with roads mask', required=True)
    parser.add_argument('-s', '--save', help='Path to file to save results', required=False)
    parser.add_argument('-t', '--temperature', help='Temperature', type=float, required=True)
    parser.add_argument('-z', '--zdimension', help='Dimension along z axis (>0)', type=int, required=False, default=80)
    parser.add_argument('-c', '--celllength', help='Cell length side', type=float, required=False, default=1)
    parser.add_argument('-k', '--kernelsize', help='Kernel size (must be odd)', type=int, required=False, default=3)
    parser.add_argument('-r', '--redfactor', help='Red channel factor', type=float, required=False, default=100)
    parser.add_argument('-g', '--greenfactor', help='Green channel factor', type=float, required=False, default=40)
    parser.add_argument('-b', '--bluefactor', help='Blue channel factor', type=float, required=False, default=5)

    args = vars(parser.parse_args())
    if args['zdimension'] <= 0 or args['celllength'] <= 0 or args['kernelsize'] <= 0:
        parser.print_help()
        sys.exit()

    kernel_size = args['kernelsize']
    d_left = (kernel_size - 1) // 2
    d_right = (kernel_size - 1) - d_left

    bitmap_image = np.array(Image.open(args['mask']))

    nx = len(bitmap_image)
    ny = len(bitmap_image[0])
    nz = int(args['zdimension']) + 1

    convoluted = []
    for channel in range(3):
        bitmap_mask = np.array(bitmap_image[:, :, channel])

        extended_bitmap_mask = np.pad(bitmap_mask, [(d_left, d_right), (d_left, d_right)], mode='constant')

        x, y = np.meshgrid(np.linspace(-1, 1, kernel_size), np.linspace(-1, 1, kernel_size))
        d = np.sqrt(x * x + y * y)
        sigma, mu = 1.0, 0.0
        conv_filter_unnormalized = np.exp(-((d - mu) ** 2 / (2.0 * sigma ** 2)))
        conv_filter = conv_filter_unnormalized / np.sum(conv_filter_unnormalized)

        sub_shape = (nx, ny)
        view_shape = tuple(np.subtract(extended_bitmap_mask.shape, sub_shape) + 1) + sub_shape
        strides = extended_bitmap_mask.strides + extended_bitmap_mask.strides

        sub_matrices = np.lib.stride_tricks.as_strided(extended_bitmap_mask, view_shape, strides)
        convoluted_channel = np.einsum('ij,ijkl->kl', conv_filter, sub_matrices)
        convoluted.append(convoluted_channel)

    convoluted_sum = args['redfactor']*convoluted[0] + args['greenfactor']*convoluted[1] + args['bluefactor']*convoluted[2]
    convoluted_max = np.max(convoluted_sum)
    convoluted_sum = convoluted_sum/convoluted_max

    img = Image.new('L', (nx, ny), "white")
    pixels = img.load()
    for i in range(nx):
        for j in range(ny):
            pixels[i, j] = int(255*convoluted_sum[j, i])
    img.save(args['save'], "bmp")
