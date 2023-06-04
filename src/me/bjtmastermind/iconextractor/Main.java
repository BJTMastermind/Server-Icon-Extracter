package me.bjtmastermind.iconextractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import me.bjtmastermind.iconextractor.gui.Window;
import me.bjtmastermind.nbt.io.NBTUtil;
import me.bjtmastermind.nbt.io.NamedTag;
import me.bjtmastermind.nbt.tag.CompoundTag;
import me.bjtmastermind.nbt.tag.ListTag;

public class Main {
    private static List<String> allowedFlags = Arrays.asList(
        "--extract-server","-e",
        "--from-file","-f",
        "--help","-h",
        "--list-servers","-l",
        "--output","-o",
        "--version","-v"
    );
    private static int maxServers = -1;
    private static String outputPath = "./";
    private static File serverList = new File(Utils.getMCPath()+"servers.dat");

    public static void main(String[] args) {
        if (args.length > 0) {
            parseArgs(args);
        } else {
            Window.spawnWindow();
        }
    }

    private static void parseArgs(String[] args) {
        for (String arg : args) {
            if (!allowedFlags.contains(arg.split("=")[0])) {
                System.err.println("Found invaild flag! '"+arg.split("=")[0]+"'");
                System.exit(1);
            }
        }
        if (args[0].equals("--help") || args[0].equals("-h")) {
            System.out.println("Usage: Server-Icon-Extractor.jar [OPTIONS]...\n");

            System.out.println("Server-Icon-Extractor.jar --extract-server, -e  Selects a server by its id number to have its icon extracted.");
            System.out.println("Server-Icon-Extractor.jar --from-file, -f       Selects a different servers.dat file.");
            System.out.println("Server-Icon-Extractor.jar --list-servers, -l    Output the list of servers from the loaded servers.dat.");
            System.out.println("Server-Icon-Extractor.jar --output, -o          Selects the output directory for the extracted server icon.");
            System.out.println("Server-Icon-Extractor.jar --help, -h            Displays this help message and exits.");
            System.out.println("Server-Icon-Extractor.jar --version, -v         Output programs version and exits.");
            System.exit(0);
        }
        if (args[0].equals("--version") || args[0].equals("-v")) {
            System.out.println("Server Icon Extractor 0.1.2");
            System.exit(0);
        }
        int selectedServer = -1;
        boolean extracting = false;
        for (String arg : args) {
            if (arg.startsWith("--extract-server") || arg.startsWith("-e")) {
                try {
                    selectedServer = Integer.parseInt(arg.split("=")[1].replace("\"", ""));
                    if (maxServers == -1) {
                        getServerListWithIds();
                    }
                    extracting = true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Flag '"+arg+"' missing value after '='\n    Example: '"+arg.split("=")[0]+"=3'");
                    System.exit(1);
                }
            }
            if (arg.startsWith("--from-file") || arg.startsWith("-f")) {
                try {
                    serverList = new File(arg.split("=")[1].replace("\"", ""));
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Flag '"+arg+"' missing value after '='\n    Example: '"+arg.split("=")[0]+"=path/to/servers.dat'");
                    System.exit(1);
                }
            }
            if (arg.equals("--list-servers") || arg.equals("-l")) {
                System.out.println(getServerListWithIds());
                System.exit(0);
            }
            if (arg.startsWith("--output") || arg.startsWith("-o")) {
                try {
                    outputPath = arg.split("=")[1].replace("\"", "");
                    if (!outputPath.endsWith(File.separator)) {
                        outputPath += File.separator;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Flag '"+arg+"' missing value after '='\n    Example: '"+arg.split("=")[0]+"=path/to/output/directory'");
                    System.exit(1);
                }
            }
        }
        if (extracting) {
            extractIcon(selectedServer);
        }
    }

    private static void extractIcon(int index) {
        if (index < 0 || index >= maxServers) {
            System.err.println("Not a vaild id must be between 0 and "+maxServers);
            System.exit(1);
        }
        try {
            NamedTag namedtag = NBTUtil.read(serverList.getAbsolutePath(), false);
            CompoundTag root = (CompoundTag) namedtag.getTag();
            ListTag<CompoundTag> serversList = (ListTag<CompoundTag>) root.getListTag("servers");
            CompoundTag server = serversList.get(index);

            String name = server.getString("name");
            byte[] iconBinary = Base64.getDecoder().decode(server.getString("icon"));

            Files.write(Paths.get(outputPath+name+".png"), iconBinary, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            System.out.println("Extracted icon '"+outputPath+name+".png'");
        } catch (IOException e) {
            ;
        }
    }

    private static String getServerListWithIds() {
        maxServers = -1;
        String list = "";
        try {
            NamedTag namedtag = NBTUtil.read(serverList.getAbsolutePath(), false);
            CompoundTag root = (CompoundTag) namedtag.getTag();
            ListTag<CompoundTag> serversList = (ListTag<CompoundTag>) root.getListTag("servers");

            int serverCount = serversList.size();
            for (int i = 0; i < serverCount; i++) {
                CompoundTag server = serversList.get(i);
                list += i+" = "+server.getString("name")+" - "+server.getString("ip")+"\n";
                maxServers++;
            }
        } catch (IOException e) {
            ;
        }
        return list;
    }
}