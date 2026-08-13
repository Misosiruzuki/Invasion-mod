package com.whammich.invasion.proxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Chooses CommonProxy vs ClientProxy without @SidedProxy.
 */
public final class ProxyInit {

    private ProxyInit() {
    }

    public static CommonProxy create() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return new ClientProxy();
        }
        return new CommonProxy();
    }
}
