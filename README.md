**使用说明**
     
一共两个jar包，和两个配置文件，配置文件和jar包放在同一个目录下即可，nettyServer部署在公网端，可以让外部请求访问到的服务器即可。nettyClient部署在能和本地服务互通的环境即可。

**特点**
与常见的内网穿透工具对比，这个工具有如下区别

1. 常见内网穿透工具，多为 server -> client 一对一的模式，而该工具可以实现 一个server对应多个client，即同一个外部端口可以转发多个客户端的待转发服务，类似注册中心的模式。
2. 在网络要求比较严格的环境下，内网穿透工具大多不允许使用，且就算实用了也会被出口网络设备检测到，为非法工具。而该工具则可以避免，基于netty实现的tcp加密长连接来转发http任务，不会被出口网络设备检测到非法穿透内网。
3. 使用便捷，只需要一台公用主机挂上server端jar包运行，本地跑一个client的jar包与server互通即可。无需复杂的配置，开箱即用。

**场景**
1. 比如前后端接口联调时，前端开发人员与后端开发人员处于不同的局域网环境下（比如手机热点之类的），就可以使用这个。只要找到一台两边都能访问到的地址，装上server端即可使用。
2. 局域网设备之间需要互通，或者局域网内设备后台接口需要单独开放出去的场景
3. 复杂的网络环境下搭建便捷的开发环境。比如需要对接的服务器和需要部署的服务器都在内网里，并且内网无法和外界通信，这种时候一般都是搭建一个内网主机或者内网云桌面做开发环境，即难搭建调式起来又复杂，而且不通外网很多软件装起来很麻烦。最好就是在本机上开发，能直接和目标主机联调。

这时候就可以利用这个工具，做几次嵌套，比如内网目标服务器作为发起请求方，向另一台内网测试主机发起请求，这时候就可以用这个工具把测试主机收到的请求代理出来到本机上（目标服务器发起请求到测试服务器的server端，然后测试服务器上的Client端代理请求到本机的server端，再本机的client端发起请求到本机的测试服务）。

或者内网目标服务器作为请求接收方，但只能接收内网测试主机的请求，这时候就可以让测试主机装一个server端和一个Client端（两个端都装在测试主机上）帮我们发起本机的请求。


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
