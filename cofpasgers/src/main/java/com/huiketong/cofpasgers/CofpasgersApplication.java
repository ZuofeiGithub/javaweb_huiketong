package com.huiketong.cofpasgers;

import com.huiketong.cofpasgers.jni_native.HelloJni;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//public class CofpasgersApplication extends SpringBootServletInitializer {
//    public static void main(String[] args) throws IOException {
//        // 程序启动入口
//        Properties properties = new Properties();
//        InputStream in = Application.class.getClassLoader().getResourceAsStream("app.properties");
//        properties.load(in);
//        SpringApplication app = new SpringApplication(Application.class);
//        app.setDefaultProperties(properties);
//        app.run(args);
//        /*EmbeddedServletContainerAutoConfiguration*/
//        //SpringApplication.run(CofpasgersApplication.class, args);
//    }
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        builder.sources(this.getClass());
//        return super.configure(builder);
//    }
//}
public class CofpasgersApplication {
    public static void main(String[] args){
        SpringApplication.run(CofpasgersApplication.class, args);
    }
}
