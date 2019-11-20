# jVision

How to build opencv.
- download opencv and opencv modules (this was tested with version 3.2)
- extract files in a build folder
- on build folder exec this command (the folling comand have options to enable intel library for pararel computation, install examples and enable eigen library for linear algebra calculations):
  >cmake -D WITH_TBB=ON -D INSTALL_C_EXAMPLES=ON -D WITH_EIGEN=ON -D BUILD_EXAMPLES=ON -D OPENCV_EXTRA_MODULES_PATH= *path to    contrib_master*/modules .. 
- >make install
