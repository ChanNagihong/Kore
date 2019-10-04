package org.xbmc.kore.host

object DefaultHostInfo {

  fun get99Server(): HostInfo {
    return HostInfo(
        Int.MAX_VALUE,
        "99Server",
        "192.168.3.95",
        0,
        8080,
        9090,
        "kodi",
        "kodi",
        "84:39:BE:68:32:13",
        9,
        true,
        9777,
        18,
        3,
        "20190621-89472b2",
        "stable",
        1569981214803,
        false
    )
  }

  fun getIMac(): HostInfo {
    return HostInfo(
        Int.MAX_VALUE,
        "99Server",
        "192.168.31.87",
        0,
        8080,
        9090,
        "kodi",
        "kodi",
        "88:6B:6E:E6:04:2E",
        9,
        true,
        9777,
        18,
        3,
        "20190621-89472b2",
        "stable",
        1569981214803,
        false
    )
  }

}