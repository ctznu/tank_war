import com.terry.tank.Dir;
import com.terry.tank.net.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TankDieMsgCodecTest {


    @Test
    public void testEncoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        UUID id = UUID.randomUUID();
        UUID bulletID = UUID.randomUUID();
        TankDieMsg msg = new TankDieMsg(bulletID, id);
        ch.pipeline().addLast(new MsgEncoder());

        ch.writeOutbound(msg);

        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        assertEquals(MsgType.TankDie, msgType);

        int length = buf.readInt();
        assertEquals(32, length);

        UUID uuid1 = new UUID(buf.readLong(), buf.readLong());
        UUID uuid2 = new UUID(buf.readLong(), buf.readLong());

        assertEquals(bulletID, uuid1);
        assertEquals(id, uuid2);
    }

    @Test
    public void testDecoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        UUID id = UUID.randomUUID();
        UUID bulletID = UUID.randomUUID();
        TankDieMsg msg = new TankDieMsg(bulletID, id);
        ch.pipeline().addLast(new MsgDecoder());

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.TankDie.ordinal());
        byte[] bytes = msg.toBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        ch.writeInbound(buf.duplicate());

        TankDieMsg msgR = ch.readInbound();

        assertEquals(id, msgR.getId());
        assertEquals(bulletID, msg.getBulletID());

    }
}
