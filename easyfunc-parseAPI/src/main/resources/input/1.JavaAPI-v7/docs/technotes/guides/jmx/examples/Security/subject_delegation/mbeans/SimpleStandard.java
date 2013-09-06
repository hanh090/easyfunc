/**
 * Simple definition of a standard MBean, named "SimpleStandard".
 *
 * The "SimpleStandard" standard MBean shows how to expose attributes
 * and operations for management by implementing its corresponding
 * "SimpleStandardMBean" management interface.
 *
 * This MBean has two attributes and one operation exposed
 * for management by a JMX agent:
 *      - the read/write "State" attribute,
 *      - the read only "NbChanges" attribute,
 *      - the "reset()" operation.
 *
 * This object also has one property and one method not exposed
 * for management by a JMX agent:
 *      - the "NbResets" property,
 *      - the "getNbResets()" method.
 */

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.NotificationBroadcasterSupport;

public class SimpleStandard
    extends NotificationBroadcasterSupport
    implements SimpleStandardMBean {

    /*
     * -----------------------------------------------------
     * CONSTRUCTORS
     * -----------------------------------------------------
     */

    /* "SimpleStandard" does not provide any specific constructors.
     * However, "SimpleStandard" is JMX compliant with regards to
     * contructors because the default contructor SimpleStandard()
     * provided by the Java compiler is public.
     */

    /*
     * -----------------------------------------------------
     * IMPLEMENTATION OF THE SimpleStandardMBean INTERFACE
     * -----------------------------------------------------
     */

    /**
     * Getter: get the "State" attribute of the "SimpleStandard" standard MBean.
     *
     * @return the current value of the "State" attribute.
     */
    public String getState() {
        return state;
    }

    /**
     * Setter: set the "State" attribute of the "SimpleStandard" standard MBean.
     *
     * @param <VAR>s</VAR> the new value of the "State" attribute.
     */
    public void setState(String s) {
        state = s;
        nbChanges++;
    }

    /**
     * Getter: get the "NbChanges" attribute of the "SimpleStandard" standard
     * MBean.
     *
     * @return the current value of the "NbChanges" attribute.
     */
    public int getNbChanges() {
        return nbChanges;
    }

    /**
     * Operation: reset to their initial values the "State" and "NbChanges"
     * attributes of the "SimpleStandard" standard MBean.
     */
    public void reset() {
        AttributeChangeNotification acn =
            new AttributeChangeNotification(this,
                                            0,
                                            0,
                                            "NbChanges reset",
                                            "NbChanges",
                                            "Integer",
                                            new Integer(nbChanges),
                                            new Integer(0));
        state = "initial state";
        nbChanges = 0;
        nbResets++;
        sendNotification(acn);
    }

    /*
     * -----------------------------------------------------
     * METHOD NOT EXPOSED FOR MANAGEMENT BY A JMX AGENT
     * -----------------------------------------------------
     */

    /**
     * Return the "NbResets" property.
     * This method is not a Getter in the JMX sense because it
     * is not exposed in the "SimpleStandardMBean" interface.
     *
     * @return the current value of the "NbResets" property.
     */
    public int getNbResets() {
        return nbResets;
    }

    /**
     * Returns an array indicating, for each notification this MBean
     * may send, the name of the Java class of the notification and
     * the notification type.</p>
     *
     * @return the array of possible notifications.
     */
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[] {
            new MBeanNotificationInfo(
            new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE },
            AttributeChangeNotification.class.getName(),
            "This notification is emitted when the reset() method is called.")
        };
    }

    /*
     * -----------------------------------------------------
     * ATTRIBUTES ACCESSIBLE FOR MANAGEMENT BY A JMX AGENT
     * -----------------------------------------------------
     */

    private String state = "initial state";
    private int nbChanges = 0;

    /*
     * -----------------------------------------------------
     * PROPERTY NOT ACCESSIBLE FOR MANAGEMENT BY A JMX AGENT
     * -----------------------------------------------------
     */

    private int nbResets = 0;
}
