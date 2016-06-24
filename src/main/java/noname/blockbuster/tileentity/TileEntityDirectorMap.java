package noname.blockbuster.tileentity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import noname.blockbuster.entity.EntityActor;
import noname.blockbuster.recording.Mocap;
import noname.blockbuster.recording.PlayThread;

/**
 * Director map tile entity
 *
 * This TE is responsible for main logic of  */
public class TileEntityDirectorMap extends AbstractTileEntityDirector
{
    /**
     * Temporary map of actor entities during playback. This map is used
     * to determine if the registered actors are still playing their roles.
     */
    protected Map<String, EntityActor> actorMap = new HashMap<String, EntityActor>();

    /**
     * Add a replay string to list of actors
     */
    public boolean add(String replay)
    {
        if (!this.actors.contains(replay))
        {
            this.actors.add(replay);
            this.markDirty();

            return true;
        }

        return false;
    }

    /**
     * Starts a playback
     *
     * This method is different from the method in DirectorTileEntity, instead
     * of finding all entities and making them play, this method is basically
     * do the same thing as CommandPlay#execute, launching the playback
     * and adding new created entity to actors map.
     */
    @Override
    public void startPlayback()
    {
        if (this.isPlaying())
        {
            return;
        }

        for (String replay : this.actors)
        {
            String[] splits = replay.split(":");
            Entity entity = null;

            String filename = splits.length >= 1 ? splits[0] : "";
            String name = splits.length >= 2 ? splits[1] : "";
            String skin = splits.length >= 3 ? splits[2] : "";
            boolean isInvulnerable = splits.length >= 4 && splits[3].equals("1");

            entity = Mocap.startPlayback(filename, name, skin, this.worldObj, true);
            entity.setEntityInvulnerable(isInvulnerable);

            this.actorMap.put(replay, (EntityActor) entity);
        }

        this.playBlock(true);
    }

    /**
     * Stop playback
     */
    @Override
    public void stopPlayback()
    {
        for (PlayThread thread : Mocap.playbacks.values())
        {
            if (this.actorMap.containsValue(thread.actor))
            {
                thread.playing = false;
            }
        }
    }

    /**
     * Does what it says to do – checking if the actors still playing their
     * roles (not finished playback).
     */
    @Override
    protected void areActorsStillPlaying()
    {
        int count = 0;

        for (String replay : this.actors)
        {
            if (Mocap.playbacks.containsKey(this.actorMap.get(replay)))
            {
                count++;
            }
        }

        if (count == 0)
        {
            this.playBlock(false);
            this.actorMap.clear();
        }
    }
}
