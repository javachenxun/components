#### Unix的IO模型分类
    fd（文件描述符）：Linux内核与外部设备一个io连接的表述;
        一个socket的读写为一个socketfd（socket描述符）
    ps：描述符就是一个数字，指向内核一个结构体（包含文件路径，数据区等属性）
    
    应用进程 <--> recvfrom函数 <--> 内核
    数据存在于应用进程的用户空间
    
 * 阻塞IO模型
    * 阻塞->就绪->recvfrom拷贝
 * 非阻塞IO模型
    * 不断探测->就绪->recvfrom拷贝
 * IO复用模型
    * select／poll 顺序扫描 ->就绪->recvfrom拷贝
    * epoll ：基于事件驱动，内核回调（JAVA NIO）->recvfrom拷贝
 * 信号驱动IO模型
    sigaction->sigio(就绪信号)->recvfrom拷贝
 * 异步IO
    通知内核启动某个操作->完成全部->通知
 


