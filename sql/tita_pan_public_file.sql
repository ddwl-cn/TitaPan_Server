create table public_file
(
    fid           int              not null,
    f_name        varchar(300)     not null,
    n_name        varchar(300)     not null,
    hot           int    default 0 not null,
    f_size        bigint default 0 not null,
    f_description varchar(3000)    null,
    upload_date   varchar(30)      not null,
    preview_url   varchar(300)     null,
    score         float  default 0 not null,
    constraint public_file_fid_uindex
        unique (fid)
)
    comment '公共文件表';

INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (812, 'wallpaper (2).jpg', 'wallpaper (2)', 2, 711865, 'wallpaper (2)', '2023-05-07 23:07', 'http://127.0.0.1:8012/PqTcN5I93gkzfkgOC2NgTFGI3edBH0rexZNA60mAHmY4yFhKmTC15hVrV17WdMcc-1683472069329-preview.jpg', 3);
INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (814, 'wallpaper (1).jpg', 'wallpaper (1)', 0, 191091, 'wallpaper (1)', '2023-05-07 23:07', 'http://127.0.0.1:8012/rxWkYmct4IvkbcRp1GzhnYiTN3lpbT5lYY3lA1JpqOQxCXoeHT3pjWJtAyEhnLWv-1683472054075-preview.jpg', 5);
INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (817, 'wallpaper (4).jpg', 'wallpaper (4)', 0, 1276632, 'wallpaper (4)', '2023-05-07 23:08', 'http://127.0.0.1:8012/hPqrJrdlHCNcK2GGgLYqruq5T53Rom9lFhoaLrxDRGdrdYixiO5XDhFVP1gS6FKR-1683472084071-preview.jpg', 0);
INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (818, 'wallpaper (5).jpg', 'wallpaper (5)', 0, 1841576, 'wallpaper (5)', '2023-05-07 23:08', 'http://127.0.0.1:8012/4NhNVw4Owp0TYUgJlldYHeZp7igQcQySiSZ53nxeUDmO3rLibbYLarH5ew87v20n-1683472100801-preview.jpg', 0);
INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (821, 'Wireshark.zip', 'wireshark', 0, 61358368, 'Wireshark（前称Ethereal）是一个网络封包分析软件。网络封包分析软件的功能是截取网络封包，并尽可能显示出最为详细的网络封包资料。Wireshark使用WinPCAP作为接口，直接与网卡进行数据报文交换。在过去，网络封包分析软件是非常昂贵的，或是专门属于盈利用的软件。
Ethereal的出现改变了这一切。在GNUGPL通用许可证的保障范围底下，使用者可以以免费的途径取得软件与其源代码，并拥有针对其源代码修改及客制化的权利。Ethereal是全世界最广泛的网络封包分析软件之一。
', '2023-05-06 19:55', 'http://127.0.0.1:8012/nQNSWKw7TjpQpguqjmqrDR46L3dKC6i73TnxjIPm23kOjfZtK0U4TXo5uu5mVhjq-1680673262853-preview.jpg', 4.66667);
INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (822, 'tomcat-native-1.2.34-openssl-1.1.1o-win32-bin.zip', 'tomcat', 0, 3631139, 'The Apache Tomcat® software is an open source implementation of the Jakarta Servlet, Jakarta Server Pages, Jakarta Expression Language, Jakarta WebSocket, Jakarta Annotations and Jakarta Authentication specifications. 
These specifications are part of the Jakarta EE platform.

The Jakarta EE platform is the evolution of the Java EE platform. Tomcat 10 and later implement specifications developed as part of Jakarta EE. Tomcat 9 and earlier implement specifications developed as part of Java EE.

The Apache Tomcat software is developed in an open and participatory environment and released under the Apache License version 2. The Apache Tomcat project is intended to be a collaboration of the best-of-breed developers from around the world. We invite you to participate in this open development project. To learn more about getting involved, click here.

Apache Tomcat software powers numerous large-scale, mission-critical web applications across a diverse range of industries and organizations. Some of these users and their stories are listed on the PoweredBy wiki page.

Apache Tomcat, Tomcat, Apache, the Apache feather, and the Apache Tomcat project logo are trademarks of the Apache Software Foundation.', '2023-05-06 20:01', 'http://127.0.0.1:8012/fYFX8TtHKklKF3cwXhw9tmvfIatXEAMC7N8rI3jrkmaH2CWIEOuyewx6p3VqK1f6-1682478894529-preview.jpg', 3.5);
INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (823, 'IntelliJ IDEA2021(64bit).zip', 'IDEA-2021', 0, 766164255, 'IntelliJ IDEA – 领先的 Java 和 Kotlin IDE
IntelliJ IDEA is undoubtedly the top-choice IDE for software developers. Efficiency and intelligence are built into the design, which enables a very smooth development workflow experience, from design, implementation, building, deploying, testing, and debugging, to refactoring! It is loaded with features and also offers a plethora of plugins that we can integrate into the editor. I switched to using IntelliJ IDEA 5 years ago and have never looked back.It has certainly made my life easier. I am producing more with less effort.', '2023-05-07 23:12', 'http://127.0.0.1:8012/skhib5z3TAZCXlmdd1FAJv21iuUCY6FQKGi5V2Dr7GjdWgQ5dn9l6sVlea4rS2tX-1682479315833-preview.jpg', 5);
INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (824, 'jdk-17_windows-x64_bin.exe', 'JDK-17', 0, 160061712, 'JDK 17 is the open-source reference implementation of version 17 of the Java SE Platform, as specified by by JSR 390 in the Java Community Process.
JDK 17 reached General Availability on 14 September 2021. Production-ready binaries under the GPL are available from Oracle; binaries from other vendors will follow shortly.
The features and schedule of this release were proposed and tracked via the JEP Process, as amended by the JEP 2.0 proposal. The release was produced using the JDK Release Process (JEP 3).', '2023-05-07 23:10', 'http://127.0.0.1:8012/aXEVBWwbzQmzcTtEQpw2oqDkv14otqTOk64DyZfChoiawF8XTeCdf78HM47iXFo1-1682479646400-preview.jpg', 1);
INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (826, '我的世界.zip', '我的世界 1.16.5', 0, 1112128897, '《我的世界》（Minecraft）是一款沙盒类电子游戏，开创者为马库斯·阿列克谢·泊松（Notch）。游戏由Mojang Studios维护，现隶属于微软Xbox游戏工作室。游戏最初于2009年5月17日作为Classic版本发布，并于2011年11月18日发布Java正式版。我的世界的游戏平台囊括桌面设备、移动设备和游戏主机。中国版现由网易游戏代理，于2016年5月20日在中国大陆运营。
自开创伊始到延斯·伯根斯坦（Jeb）加入并负责开发之前，我的世界几乎全部的开发工作由Notch完成。游戏音乐由丹尼尔·罗森菲尔德和莉娜·雷恩（Lena Raine）创作；克里斯托弗·泽特斯特兰绘制了游戏中的画。
该游戏以玩家在一个充满着方块的三维空间中自由地创造和破坏不同种类的方块为主题。玩家在游戏中可以在单人或多人模式中通过摧毁或创造精妙绝伦的建筑物和艺术，或者收集物品探索地图以完成游戏的成就（进度）。玩家也可以尝试在创造模式下(打开作弊)红石电路和指令等玩法。
我的世界1.16.5正式版是一款非常不错的冒险像素类游戏,游戏中能够带给你全部的新玩法和新特色,不仅很刺激而且炫酷,不会有规则和限制妨碍你全都能够让你自己来建造,快来下载试试吧。', '2023-05-06 20:37', 'http://127.0.0.1:8012/QnymkpUQ5jNnpyTlqgJCVvxpCsdwAOAVGI3MBC1sfJMc2XdrUkjYU3hxSDsT0VSv-1683375410264-preview.jpg', 0);
INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (855, 'mysql-8.0.20-1.el7.x86_64.rpm-bundle.tar', 'MySql 8.0 --- tar压缩文件', 0, 834560000, 'MySQL HeatWave is a fully managed service that enables customers to run OLTP, OLAP, and machine learning workloads directly from their MySQL Database. HeatWave boosts MySQL performance by 5400x.', '2023-05-09 21:10', 'http://127.0.0.1:8012/Et18TJz6L2XIqGVlUHtU5tHT2i5pg3ix0QNCHom7H0PMAHAF4EsMQoVME4qFTIfJ-1683637703514-preview.jpg', 0);
INSERT INTO tita_pan.public_file (fid, f_name, n_name, hot, f_size, f_description, upload_date, preview_url, score) VALUES (856, '图吧工具箱2023.05绿色版自动解压程序R3.zip', '图吧工具箱2023(绿色自动解压版)', 1, 203890167, '图吧工具箱，是开源、免费、绿色、纯净的硬件检测工具合集，专为所有计算机硬件极客、DIY爱好者、各路大神及小白制作。集成大量常见硬件检测、评分工具，一键下载、方便使用。

专业 · 专注于收集各种硬件检测、评分、测试工具，常见工具均有收集。
纯净 · 无任何捆绑强制安装行为，不写入注册表，没有任何敏感目录及文件操作，无任何诱导、孔吓、欺乍等操作。
绿色 · 仅提供自解压格式的压缩包(可右键使用任意解压工具打开)，无需安装、注册等复杂操作，解压即可使用。用完可直接删除，无需卸载。', '2023-05-09 21:11', 'http://127.0.0.1:8012/Xun3wsGTkpb8JkQcKfmcqI969spazyboAhFO3ug9K37hVf6k6NKVjx6TJnAD6pwI-1683637914007-preview.jpg', 0);