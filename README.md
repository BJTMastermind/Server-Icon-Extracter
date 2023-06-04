# Server Icon Extractor

  A simple tool that allows you to extract a Minecraft: Java Edition servers icon.                                                                                         

## To Do

- [x] Add Program Icon
- [x] Command Line Interface

## How To Use (UI)

Coming Soon. (In progress)

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
