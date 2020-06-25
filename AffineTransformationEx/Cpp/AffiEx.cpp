//============================================================================
// Name        : AffiEx.cpp
// Author      : Depeng Liang
//============================================================================

#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

int main(int argc, char** argv) {

  if (argc != 11) {
    std::cout << "usage: AffiEx IMAGE BBOX_TOP_LEFT_X BBOX_TOP_LEFT_Y BBOX_W BBOX_H ROTATE_ANGLE SHEAR_FACTOER OUT_W OUT_H OUT_PATH" << std::endl;
    exit(0);
  }

  cv::Mat image;
  image = cv::imread(argv[1], CV_LOAD_IMAGE_COLOR);   // Read the file

  cv::namedWindow( "origin image", cv::WINDOW_AUTOSIZE );// Create a window for display.
  cv::imshow( "origin image", image );                   // Show our image inside it.

  float bbox_top_x = atoi(argv[2]);
  float bbox_top_y = atoi(argv[3]);
  float bbox_w = atoi(argv[4]);
  float bbox_h = atoi(argv[5]);
  float bbox_center_x = bbox_top_x + bbox_w / 2;
  float bbox_center_y = bbox_top_y + bbox_h / 2;

  float new_width = atoi(argv[8]);
  float new_height = atoi(argv[9]);

  // crop mat
  cv::Mat crop_mat(3, 3, CV_32F);
  // set default value of crop_mat
  crop_mat.at<float>(0, 0) = 1;
  crop_mat.at<float>(0, 1) = 0;
  crop_mat.at<float>(0, 2) = 0;
  crop_mat.at<float>(1, 0) = 0;
  crop_mat.at<float>(1, 1) = 1;
  crop_mat.at<float>(1, 2) = 0;
  crop_mat.at<float>(2, 0) = 0;
  crop_mat.at<float>(2, 1) = 0;
  crop_mat.at<float>(2, 2) = 1;

  crop_mat.at<float>(0, 2) = -bbox_top_x;
  crop_mat.at<float>(1, 2) = -bbox_top_y;

  // scale mat
  cv::Mat scale_mat(3, 3, CV_32F);
  // set default value of scale_mat
  scale_mat.at<float>(0, 0) = 1;
  scale_mat.at<float>(0, 1) = 0;
  scale_mat.at<float>(0, 2) = 0;
  scale_mat.at<float>(1, 0) = 0;
  scale_mat.at<float>(1, 1) = 1;
  scale_mat.at<float>(1, 2) = 0;
  scale_mat.at<float>(2, 0) = 0;
  scale_mat.at<float>(2, 1) = 0;
  scale_mat.at<float>(2, 2) = 1;

  scale_mat.at<float>(0, 0) = new_width / bbox_w;
  scale_mat.at<float>(1, 1) = new_height / bbox_h;

  // shift mat
  cv::Mat shift_mat1(3, 3, CV_32F);
  // set default value of shift_mat1
  shift_mat1.at<float>(0, 0) = 1;
  shift_mat1.at<float>(0, 1) = 0;
  shift_mat1.at<float>(0, 2) = 0;
  shift_mat1.at<float>(1, 0) = 0;
  shift_mat1.at<float>(1, 1) = 1;
  shift_mat1.at<float>(1, 2) = 0;
  shift_mat1.at<float>(2, 0) = 0;
  shift_mat1.at<float>(2, 1) = 0;
  shift_mat1.at<float>(2, 2) = 1;

  shift_mat1.at<float>(0, 2) = -(new_width / 2);
  shift_mat1.at<float>(1, 2) = -(new_height / 2);

  // rotate mat
  cv::Mat rotate_mat(3, 3, CV_32F);
  // set default value of rotate_mat
  rotate_mat.at<float>(0, 0) = 1;
  rotate_mat.at<float>(0, 1) = 0;
  rotate_mat.at<float>(0, 2) = 0;
  rotate_mat.at<float>(1, 0) = 0;
  rotate_mat.at<float>(1, 1) = 1;
  rotate_mat.at<float>(1, 2) = 0;
  rotate_mat.at<float>(2, 0) = 0;
  rotate_mat.at<float>(2, 1) = 0;
  rotate_mat.at<float>(2, 2) = 1;

  float angle = atoi(argv[6]);
  angle = angle / 180.0 * 3.14159265358979323846;
  const float cos_a = std::cos(angle);
  const float sin_a = std::sin(angle);

  rotate_mat.at<float>(0, 0) = cos_a;
  rotate_mat.at<float>(0, 1) = sin_a;
  rotate_mat.at<float>(1, 0) = -sin_a;
  rotate_mat.at<float>(1, 1) = cos_a;

  // shear mat
  cv::Mat shear_mat(3, 3, CV_32F);
  // set default value of shear_mat
  shear_mat.at<float>(0, 0) = 1;
  shear_mat.at<float>(0, 1) = 0;
  shear_mat.at<float>(0, 2) = 0;
  shear_mat.at<float>(1, 0) = 0;
  shear_mat.at<float>(1, 1) = 1;
  shear_mat.at<float>(1, 2) = 0;
  shear_mat.at<float>(2, 0) = 0;
  shear_mat.at<float>(2, 1) = 0;
  shear_mat.at<float>(2, 2) = 1;

  float shear_factor = atof(argv[7]);
  shear_mat.at<float>(0, 1) = shear_factor;
  shear_mat.at<float>(1, 0) = shear_factor;

  // shift mat
  cv::Mat shift_mat2(2, 3, CV_32F);
  // set default value of shift_mat2
  shift_mat2.at<float>(0, 0) = 1;
  shift_mat2.at<float>(0, 1) = 0;
  shift_mat2.at<float>(0, 2) = 0;
  shift_mat2.at<float>(1, 0) = 0;
  shift_mat2.at<float>(1, 1) = 1;
  shift_mat2.at<float>(1, 2) = 0;

  shift_mat2.at<float>(0, 2) = new_width / 2;
  shift_mat2.at<float>(1, 2) = new_height / 2;

  cv::Mat out;
  cv::Mat tran_mat = shift_mat2 * shear_mat * rotate_mat * shift_mat1 * scale_mat * crop_mat;

  cv::Size new_size(new_width, new_height);
  cv::warpAffine(image, out, tran_mat, new_size, CV_INTER_LINEAR, cv::BORDER_CONSTANT,
           cv::Scalar(0, 0, 0));

  cv::imwrite(argv[10], out);
  cv::namedWindow( "affi image", cv::WINDOW_AUTOSIZE );
  cv::imshow( "affi image", out);
  cv::waitKey();

  return 0;
}
