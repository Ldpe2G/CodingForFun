import cv2
import argparse
import math
import numpy as np

def parse_args():
  parser = argparse.ArgumentParser(description='')
  parser.add_argument('--image', type=str, default='../datas/timg.jpg', help='')
  parser.add_argument('--out-path', type=str, default='../datas/cropped.jpg', help='')
  parser.add_argument('--bbox', type=str, default='175,30,250,270', help='')
  parser.add_argument('--rotate-angle', type=float, default=10, help='')
  parser.add_argument('--out-wh', type=str, default='250,270', help='')
  parser.add_argument('--shear-factor', type=float, default=0.1, help='')
  return parser.parse_args()

if __name__ == '__main__':
  args = parse_args()

  img = cv2.imread(args.image, 1)

  bbox = [int(e) for e in args.bbox.split(',')]
  out_wh = [int(e) for e in args.out_wh.split(',')]

  # crop matrix, mapping the center of bbox to the center of croped image
  crop_mat = np.zeros((3, 3), np.float32)
  crop_mat[0][0] = 1
  crop_mat[1][1] = 1
  crop_mat[2][2] = 1

  bbox_center_x = bbox[0] + bbox[2] / 2
  bbox_center_y = bbox[1] + bbox[3] / 2

  src_pt = [bbox_center_x, bbox_center_y]
  dst_pt = [out_wh[0] / 2, out_wh[1] / 2]

  scale_x = float(out_wh[0]) / bbox[2]
  scale_y = float(out_wh[1]) / bbox[3]

  crop_mat[0][0] = scale_x
  crop_mat[1][1] = scale_y

  crop_mat[0][2] = dst_pt[0] - crop_mat[0][0] * src_pt[0]
  crop_mat[1][2] = dst_pt[1] - crop_mat[1][1] * src_pt[1]

  # shift matrix 
  shift_mat1 = np.zeros((3, 3), np.float32)
  shift_mat1[0][0] = 1
  shift_mat1[1][1] = 1
  shift_mat1[2][2] = 1

  shift_mat1[0][2] = -dst_pt[0]
  shift_mat1[1][2] = -dst_pt[1]

  # rotate matrix
  rotate_mat = np.zeros((3, 3), np.float32)
  rotate_mat[0][0] = 1
  rotate_mat[1][1] = 1
  rotate_mat[2][2] = 1

  angle = args.rotate_angle / 180.0 * 3.14159265358979323846
  cos_a = math.cos(angle)
  sin_a = math.sin(angle)

  rotate_mat[0][0] = cos_a
  rotate_mat[0][1] = sin_a
  rotate_mat[1][0] = -sin_a
  rotate_mat[1][1] = cos_a

  # shear matrix
  shear_mat = np.zeros((3, 3), np.float32)
  shear_mat[0][0] = 1
  shear_mat[1][1] = 1
  shear_mat[2][2] = 1

  shear_x = args.shear_factor

  shear_mat[0][1] = shear_x
  shear_mat[1][0] = shear_x

  # shift matrix 
  shift_mat2 = np.zeros((2, 3), np.float32)
  shift_mat2[0][0] = 1
  shift_mat2[1][1] = 1

  shift_mat2[0][2] = dst_pt[0]
  shift_mat2[1][2] = dst_pt[1]

  tran_mat = cv2.gemm(shift_mat2, shear_mat, 1, None, 0) 
  tran_mat = cv2.gemm(tran_mat, rotate_mat, 1, None, 0) 
  tran_mat = cv2.gemm(tran_mat, shift_mat1, 1, None, 0) 
  tran_mat = cv2.gemm(tran_mat, crop_mat, 1, None, 0) 

  out = cv2.warpAffine(img, tran_mat, (out_wh[0], out_wh[1]))

  cv2.imshow('original image', img)
  cv2.imshow('cropped image', out)
  cv2.imwrite(args.out_path, out)
  cv2.waitKey()

      

