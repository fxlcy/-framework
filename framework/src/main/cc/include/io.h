//
// Created by fxlcy on 2016/10/30.
//

#ifndef MYAPPLICATION_IO_H
#define MYAPPLICATION_IO_H

/*
函数入口：文件夹的绝对路径
          const char*  dirPath

函数功能：删除该文件夹，包括其中所有的文件和文件夹

返回值：  0  删除
         -1  路径不对，或其它情况，没有执行删除操作
*/
int rmNotEmptyDir(const char* dir);


#endif //MYAPPLICATION_IO_H
