/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityList
 *  net.minecraft.world.World
 */
package invmod.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.world.World;

public class SimplyID {
    private static int nextSimplyID;
    private static Set<String> loadedIDs;
    private static String loadedWorld;
    private static File file;
    private static PrintWriter writer;

    public static String getNextSimplyID(Entity par1Entity) {
        String id;
        SimplyID.loadSession(par1Entity.field_70170_p);
        int i = nextSimplyID = 0;
        while (!loadedIDs.add(id = EntityList.func_75621_b((Entity)par1Entity) + nextSimplyID++)) {
        }
        SimplyID.writeIDToFile(id);
        return id;
    }

    public static void loadSession(World worldObj) {
        if (loadedWorld == null || !worldObj.func_72860_G().func_75760_g().equals(loadedWorld)) {
            SimplyID.resetSimplyIDTo(worldObj);
        }
    }

    public static void resetSimplyIDTo(World world) {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
        loadedIDs.clear();
        loadedWorld = world.func_72860_G().func_75760_g();
        String directory = "saves/" + loadedWorld + "/";
        file = new File(directory + "savedIDs.txt");
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        SimplyID.populateSet();
    }

    public static void writeIDToFile(String id) {
        writer.println(id);
        writer.flush();
    }

    public static void populateSet() {
        FileReader pre = null;
        BufferedReader reader = null;
        try {
            pre = new FileReader(file);
            reader = new BufferedReader(pre);
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("delete ")) {
                        SimplyID.deleteID(line, false);
                        continue;
                    }
                    SimplyID.addID(line);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            // empty catch block
        }
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        SimplyID.refreshLoadedIDFile();
    }

    private static void refreshLoadedIDFile() {
        try {
            PrintWriter writer = new PrintWriter(file);
            for (String id : loadedIDs) {
                writer.println(id);
            }
            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getLoadedIDs() {
        return loadedIDs;
    }

    public static void setLoadedIDs(Set<String> _loadedIDs) {
        loadedIDs = _loadedIDs;
    }

    public static void addID(String newID) {
        loadedIDs.add(newID);
    }

    public static void deleteID(String deletedID, Boolean flag) {
        if (!flag.booleanValue() && deletedID.startsWith("delete ")) {
            deletedID = deletedID.split(" ")[1];
        }
        if (flag.booleanValue()) {
            SimplyID.writeIDToFile("delete " + deletedID);
        }
        loadedIDs.remove(deletedID);
    }

    public static void deleteID(World world, String string) {
        SimplyID.loadSession(world);
        SimplyID.deleteID(string, true);
    }

    static {
        loadedIDs = new HashSet<String>();
        loadedWorld = null;
        file = null;
        writer = null;
    }
}

