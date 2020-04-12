import com.terry.tank.Dir;
import com.terry.tank.Tank;
import com.terry.tank.net.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TankDirChangedMsgCodecTest {


    @Test
    public void testEncoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        UUID id = UUID.randomUUID();
        TankDirChangedMsg msg = new TankDirChangedMsg(id, 10, 20, Dir.UP);
        ch.pipeline().addLast(new MsgEncoder());

        ch.writeOutbound(msg);

        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        assertEquals(MsgType.TankDirChanged, msgType);

        int length = buf.readInt();
        assertEquals(28, length);

        UUID uuid = new UUID(buf.readLong(), buf.readLong());
        int x = buf.readInt();
        int y = buf.readInt();
        Dir dir = Dir.values()[buf.readInt()];

        assertEquals(id, uuid);
        assertEquals(10, x);
        assertEquals(20, y);
        assertEquals(Dir.UP, dir);
    }

    @Test
    public void testDecoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        UUID id = UUID.randomUUID();
        TankDirChangedMsg msg = new TankDirChangedMsg(id, 10, 20, Dir.UP);
        ch.pipeline().addLast(new MsgDecoder());

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.TankDirChanged.ordinal());
        byte[] bytes = msg.toBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        ch.writeInbound(buf.duplicate());

        TankDirChangedMsg msgR = ch.readInbound();

        assertEquals(id, msgR.getId());
        assertEquals(10, msgR.getX());
        assertEquals(20, msgR.getY());
        assertEquals(Dir.UP, msgR.getDir());

    }
}
