import java.io.File;
import java.util.HashMap;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class Server {

    public static void main(String[] args) {
        try {
            // Instantiate the MBean server
            //
            System.out.println("\nCreate the MBean server");
            MBeanServer mbs = MBeanServerFactory.createMBeanServer();

            // Environment map
            //
            System.out.println("\nInitialize the environment map");
            HashMap env = new HashMap();

            // Provide SSL-based RMI socket factories.
            //
            SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
            SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory();
            env.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE,csf);
            env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE,ssf);

            // Provide the password file used by the connector server to
            // perform user authentication. The password file is a properties
            // based text file specifying username/password pairs. This
            // properties based password authenticator has been implemented
            // using the JMXAuthenticator interface and is passed to the
            // connector through the "jmx.remote.authenticator" property
            // in the map.
            //
            // This property is implementation-dependent and might not be
            // supported by all implementations of the JMX Remote API.
            //
            env.put("jmx.remote.x.password.file",
                    "config" + File.separator + "password.properties");

            // Create an RMI connector server
            //
            System.out.println("\nCreate an RMI connector server");
            JMXServiceURL url = new JMXServiceURL(
              "service:jmx:rmi:///jndi/rmi://localhost:9999/server");
            JMXConnectorServer cs =
                JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);

            // Start the RMI connector server
            //
            System.out.println("\nStart the RMI connector server");
            cs.start();
            System.out.println("\nRMI connector server successfully started");
            System.out.println("\nWaiting for incoming connections...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
