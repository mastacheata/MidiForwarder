import javax.sound.midi.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: benedikt
 * Date: 12.12.13
 * Time: 23:39
 */
public class MappingReceiver implements Receiver {
    private JButton button;

    public MappingReceiver(JButton button) {
        this.button = button;
    }

    public MappingReceiver()
    {
    }

    public void close()
    {
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        String msgparts = "";
        if(message instanceof ShortMessage)
        {
            ShortMessage sm = ((ShortMessage) message);
            msgparts += String.valueOf(sm.getChannel());
            msgparts += String.valueOf(sm.getCommand());
            msgparts += String.valueOf(sm.getData1());
            msgparts += String.valueOf(sm.getData2());
        }
        else if (message instanceof SysexMessage)
        {
            SysexMessage mmc = ((SysexMessage)message);
            byte[]	data = mmc.getData();
            if (message.getStatus() == SysexMessage.SYSTEM_EXCLUSIVE)
            {
                msgparts += getHexString(data);
            }
        }
        else if (message instanceof MetaMessage)
        {
            System.out.println(((MetaMessage) message).getType());
        }

        if (button != null)
        {
            MidiForwarder.trigger(msgparts, button);
        }
        else
        {
            MidiForwarder.trigger(msgparts);
        }
    }

    private static char hexDigits[] =
            {'0', '1', '2', '3',
                    '4', '5', '6', '7',
                    '8', '9', 'A', 'B',
                    'C', 'D', 'E', 'F'};

    private static String getHexString(byte[] aByte)
    {
        StringBuffer sbuf = new StringBuffer(aByte.length * 3 + 2);
        for (int i = 0; i < aByte.length; i++)
        {
            sbuf.append(' ');
            sbuf.append(hexDigits[(aByte[i] & 0xF0) >> 4]);
            sbuf.append(hexDigits[aByte[i] & 0x0F]);
			/*byte	bhigh = (byte) ((aByte[i] &  0xf0) >> 4);
			sbuf.append((char) (bhigh > 9 ? bhigh + 'A' - 10: bhigh + '0'));
			byte	blow = (byte) (aByte[i] & 0x0f);
			sbuf.append((char) (blow > 9 ? blow + 'A' - 10: blow + '0'));*/
        }
        return new String(sbuf);
    }
}
