cmake_minimum_required(VERSION 3.0)
project(cmake_AffiEx)

set(CMAKE_CXX_STANDARD 11)

find_package(OpenCV REQUIRED)
message(STATUS "Found OpenCV (${OpenCV_CONFIG_PATH}), includes: ${OpenCV_INCLUDE_DIRS}, libs: ${OpenCV_LIBRARIES}")

include_directories(${OpenCV_INCLUDE_DIRS})

SET(CMAKE_CXX_FLAGS_DEBUG " $ENV{CXXFLAGS}  -DDEBUG -g -w")
SET(CMAKE_CXX_FLAGS_RELEASE " $ENV{CXXFLAGS}   -DNDEBUG -O3 -w")
SET(PLATFORM ${CMAKE_SYSTEM_NAME}-${CMAKE_SYSTEM_PROCESSOR})

add_compile_options(-std=c++11)
INCLUDE_DIRECTORIES(/usr/local/include)
LINK_DIRECTORIES(/usr/local/lib /usr/lib)

LIST(APPEND LIBS ${OpenCV_LIBRARIES} )

add_definitions(-DBUILD_STANDALONE=0)
file(GLOB_RECURSE tools_srcs ${CMAKE_CURRENT_SOURCE_DIR}/AffiEx*.cpp)

# Build each source file independently
foreach (source ${tools_srcs})
    message(STATUS "${source}")
    get_filename_component(name ${source} NAME_WE)

    # target
    add_executable(${name} ${source})
    target_link_libraries(${name} ${LIBS})
endforeach (source)
