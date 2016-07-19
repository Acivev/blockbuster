package noname.blockbuster.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noname.blockbuster.Blockbuster;
import noname.blockbuster.item.ItemPlayback;

/**
 * <p>
 * This block is responsible for launching and stopping scene recording/play.
 * It's basically the block that ties everything together.
 * </p>
 *
 * <p>
 * It also has two hooks:
 * </p>
 *
 * <ul>
 * <li>Start hook – director block sends redstone signal on east side when the
 * scene starts playing</li>
 * <li>Stop hook – director block sends redstone signal on west side when the
 * scene stops playing</li>
 * </ul>
 *
 * <p>
 * Stop hook is very useful when you need to reset the scene, like you want to
 * use TNT in your scene, but don't want to rebuild the same house over and over
 * again, you can use this redstone hook to /clone already built environment
 * after the scene was stopped.
 * </p>
 *
 * <p>
 * I don't really know a good use for the start hook, maybe start playing one of
 * these sick minecraft crafted tunes or do something else. I added just to
 * complement the stop hook.
 * </p>
 */
public abstract class AbstractBlockDirector extends Block implements ITileEntityProvider
{
    public static final PropertyBool PLAYING = PropertyBool.create("playing");

    public AbstractBlockDirector()
    {
        super(Material.ROCK);
        this.setDefaultState(this.getDefaultState().withProperty(PLAYING, false));
        this.setCreativeTab(Blockbuster.blockbusterTab);
        this.setHardness(8);
    }

    /* States */

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(PLAYING) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(PLAYING, meta == 1 ? true : false);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {PLAYING});
    }

    /* Redstone */

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return true;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    /**
     * Power west side of the block while block is playing and power east side
     * of the block while isn't playback actors.
     */
    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        boolean isPlaying = blockState.getValue(PLAYING);

        return (isPlaying && side == EnumFacing.WEST) || (!isPlaying && side == EnumFacing.EAST) ? 15 : 0;
    }

    /* Player interaction */

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }

        ItemStack item = playerIn.getHeldItemMainhand();

        if (item != null && this.handleItem(item, worldIn, pos, playerIn))
        {
            return true;
        }

        this.outputCast(playerIn, worldIn, pos);

        return true;
    }

    /**
     * Handle item on block activated.
     *
     * Used by children classes.
     */
    protected boolean handleItem(ItemStack item, World world, BlockPos pos, EntityPlayer player)
    {
        return this.handlePlaybackItem(item, pos, player);
    }

    /**
     * Attach recording item to current director block
     */
    protected boolean handlePlaybackItem(ItemStack item, BlockPos pos, EntityPlayer player)
    {
        if (!(item.getItem() instanceof ItemPlayback))
        {
            return false;
        }

        NBTTagCompound tag = item.getTagCompound();

        if (tag == null)
        {
            item.setTagCompound(tag = new NBTTagCompound());
        }

        tag.setInteger("DirX", pos.getX());
        tag.setInteger("DirY", pos.getY());
        tag.setInteger("DirZ", pos.getZ());

        player.addChatMessage(new TextComponentTranslation("blockbuster.director.attached_device"));
        return true;
    }

    /**
     * Output actors
     *
     * Temporary solution for browsing registered entities by DirectorTileEntity.
     */
    protected abstract void outputCast(EntityPlayer playerIn, World worldIn, BlockPos pos);
}
