**使用说明**
     
一共两个jar包，和两个配置文件，配置文件和jar包放在同一个目录下即可，nettyServer部署在公网端，可以让外部请求访问到的服务器即可。nettyClient部署在能和本地服务互通的环境即可。

**特点**
与常见的内网穿透工具对比，这个工具有如下区别

1. 常见内网穿透工具，多为 server -> client 一对一的模式，而该工具可以实现 一个server对应多个client，即同一个外部端口可以转发多个客户端的待转发服务，类似注册中心的模式。
2. 在网络要求比较严格的环境下，内网穿透工具大多不允许使用，且就算实用了也会被出口网络设备检测到，为非法工具。而该工具则可以避免，基于netty实现的tcp加密长连接来转发http任务，不会被出口网络设备检测到非法穿透内网。
3 使用便捷，只需要一台公用主机挂上server端jar包运行，本地跑一个client的jar包与server互通即可。无需复杂的配置，开箱即用。


## nettyServer配置文件说明
   
  **PublicPort** 默认19191，是外部请求访问的端口
  
  **PrivatePort** 默认9191，是nettyClient与nettyServer互通的端口，需与nettyClient的配置文件一致
  
  **HttpProxy** 默认true，表示开启多客户端注册，false表示单客户端
          
          说明： 当开启多客户端注册时，需配置nettyClient的ClientName，访问对应接口服务时，通过clientName来区分请求归属
          如访问clientName=kcang的服务端接口 /api/get 时 需要在路由前增加clientName前缀，即当对nettyServer公网端发起
          请求为 /kcang/api/get, nettyServer公网端会将此请求 /api/get 转发到clientName=kcang 的客户端去。
          设置为false，关闭多客户端注册时，一个nettyServer只对应一个nettyClient，请求nettyServer时就无需增加接口前缀，
          直接发起/api/get即可。同时当已经有nettyClient与nettyServer接通时，其他client无法接入server。
  
  **AesOpen** 默认true，表示开启server与client的通信加密，报文会以aes加密来通信。
       
          注意：此设置必须与client端一致，否则server端开启加密client未开启，则client端会因为无法解密报文而转发失败。同
          理server端未开启client端开启，则服务端会因为无法解密报文而转发失败。
      
  **AesKey** 当且仅当AesOpen=true时生效，为aes加密密钥，必须server和client端一致，否则报文解密失败。
      
## nettyClient配置文件说明

  **PublicAddress** 默认127.0.0.1，是nettyServer的地址
      
   **PublicPort** 默认9191，是nettyServer端与nettyClient通信的端口
      
   **PrivateAddress** 默认127.0.0.1，是待转发服务的地址
      
   **PrivatePort** 默认8080，是待转发服务的端口
      
   **ClientName** 默认kcang，当Server端开启多客户端注册时，标识当前客户端的命名
      
   **AesOpen** 默认true，开启server端与client端的通信加密
      
   **AesKey** 当且仅当AesOpen=true时生效，为aes加密密钥，必须server和client端一致，否则报文解密失败。
