package com.sgannu.blog.repo;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;


// @Configuration
public class EmbeddedMongoDB implements InitializingBean, DisposableBean {

    MongodExecutable mongodExecutable;
    public static int PORT = 27019;
    public static String HOST = "localhost";

    @Override
    public void afterPropertiesSet() throws Exception {

        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.V4_0_2)
                .net(new Net(HOST, PORT, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
    }

    @Override
    public void destroy() throws Exception {
        mongodExecutable.stop();
    }
}
