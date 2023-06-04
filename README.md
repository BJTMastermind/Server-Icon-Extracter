# Server Icon Extractor

  A simple tool that allows you to extract a Minecraft: Java Edition servers icon.                                                                                         

## How To Use (UI)

When you first open the program you will see a list of all your servers on the left. If you don't use the default Minecraft Launcher or use multiple you can click the "Import" button and select the `servers.dat` file that contains the server you want to extract the icon from.

![sie_1](https://github.com/BJTMastermind/Server-Icon-Extractor/assets/18742837/7922f7ee-402a-4a61-87ef-829ae3e53126)

Once you find the server in the list you want to extract the icon from click it, then click the "Extract" button and choose the output location. The file name will be the same as its displayed in the program.

![sie_2](https://github.com/BJTMastermind/Server-Icon-Extractor/assets/18742837/35c152d6-2c9b-46b7-a1f4-b53b66f3670c)

Done!

## How To Use (Command Line)

First print out the list of server so we can see their Ids using<br>
```sh
java -jar Server_Icon_Extractor-v0.1.2.jar --list-servers
or
java -jar Server_Icon_Extractor-v0.1.2.jar -l
```

Then once you found the server id you want to extract run<br>
```sh
java -jar Server_Icon_Extractor-v0.1.2.jar --extract-server=<server id>
or
java -jar Server_Icon_Extractor-v0.1.2.jar -e=<server id>
```

Optionally you can select a different output directory by adding `--output=output/path` or `-o=output/path` to the command.

If you want to extract a server that is not from the default Minecraft Launcher you can add `--from-file=path/to/other/servers.dat` or `-f=path/to/other/servers.dat`

## Minimum Java Version

* Java 17
