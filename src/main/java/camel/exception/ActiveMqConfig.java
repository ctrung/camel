package camel.exception;

import java.util.Arrays;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ActiveMqConfig {

	@Bean(name="activeMQConnectionFactory")
    public ActiveMQConnectionFactory activeMQConnectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("vm://localhost");
        activeMQConnectionFactory.setTrustedPackages(Arrays.asList(Invoice.class.getPackage().getName()));
        return activeMQConnectionFactory;

    }


    @Bean(initMethod = "start",destroyMethod = "stop")
    @Primary
    public org.apache.activemq.pool.PooledConnectionFactory pooledConnectionFactory(@Qualifier("activeMQConnectionFactory") ActiveMQConnectionFactory acf){
        PooledConnectionFactory pcf = new PooledConnectionFactory();
        pcf.setMaxConnections(8);
        pcf.setConnectionFactory(acf);
        return pcf;
    }

    
	@Bean
    public JmsConfiguration jmsConfiguration(PooledConnectionFactory pcf){
        JmsConfiguration jmsConf = new JmsConfiguration();
        jmsConf.setConnectionFactory(pcf);
        jmsConf.setConcurrentConsumers(10);
        return jmsConf;
    }

    @Bean
    public ActiveMQComponent activeMQComponent(JmsConfiguration jmsConfig)
    {
        ActiveMQComponent activeMQComponent= new ActiveMQComponent();
        activeMQComponent.setBrokerURL("vm://localhost");
        activeMQComponent.setConfiguration(jmsConfig);
        return activeMQComponent;
    }
    
}
