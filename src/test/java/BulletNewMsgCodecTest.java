import com.terry.tank.Bullet;
import com.terry.tank.Dir;
import com.terry.tank.Group;
import com.terry.tank.TankFrame;
import com.terry.tank.net.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class BulletNewMsgCodecTest {


    @Test
    public void testEncoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        UUID playerID = UUID.randomUUID();
        BulletNewMsg msg = new BulletNewMsg(new Bullet(playerID, 20, 30, Dir.UP, Group.GOOD, TankFrame.INSTANCE));
        ch.pipeline().addLast(new MsgEncoder());

        ch.writeOutbound(msg);

        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        assertEquals(MsgType.BulletNew, msgType);

        int length = buf.readInt();
        assertEquals(48, length);

        UUID uuid1 = new UUID(buf.readLong(), buf.readLong());
        UUID uuid2 = new UUID(buf.readLong(), buf.readLong());
        int x = buf.readInt();
        int y = buf.readInt();
        Dir dir = Dir.values()[buf.readInt()];
        Group group = Group.values()[buf.readInt()];


        assertEquals(playerID, uuid1);
        assertEquals(x, 20);
        assertEquals(y, 30);
        assertEquals(Dir.UP, dir);
        assertEquals(Group.GOOD, group);


    }

    @Test
    public void testDecoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        UUID playerID = UUID.randomUUID();
        BulletNewMsg msg = new BulletNewMsg(new Bullet(playerID, 20, 30, Dir.UP, Group.GOOD, TankFrame.INSTANCE));
        ch.pipeline().addLast(new MsgDecoder());

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.BulletNew.ordinal());
        byte[] bytes = msg.toBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        ch.writeInbound(buf.duplicate());

        BulletNewMsg msgR = ch.readInbound();

        assertEquals(playerID, msg.getPlayerID());
        assertEquals(20, msg.getX());
        assertEquals(30, msgR.getY());
        assertEquals(Dir.UP, msgR.getDir());
        assertEquals(Group.GOOD, msgR.getGroup());

    }
}
