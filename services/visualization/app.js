const express = require('express')
const app = express()
const path = require('path')

app.use(express.static(__dirname + '/public'))
app.use('/build/', express.static(path.join(__dirname, 'node_modules/three/build')))
app.use('/controls/', express.static(path.join(__dirname, 'node_modules/three/examples/jsm/controls')))

app.listen(3000, () => console.log('To view the frames of your simulation, visit http://127.0.0.1:3000'))