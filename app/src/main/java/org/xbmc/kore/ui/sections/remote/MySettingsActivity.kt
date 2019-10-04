package org.xbmc.kore.ui.sections.remote

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import org.xbmc.kore.R
import org.xbmc.kore.databinding.ActivityMySettingsBinding
import org.xbmc.kore.host.HostManager
import org.xbmc.kore.jsonrpc.method.VideoLibrary
import org.xbmc.kore.ui.BaseActivity
import org.xbmc.kore.ui.sections.hosts.HostManagerActivity

class MySettingsActivity : BaseActivity() {

  private lateinit var binding: ActivityMySettingsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_my_settings)
    val adapter = MySettingsAdapter { index, title ->
      when (title) {
        "Host" -> {
          val hostsIntent = Intent(this, HostManagerActivity::class.java)
              .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
          startActivity(hostsIntent)
        }
        "Remote control" -> {
          startActivity(Intent(this, RemoteControlActivity::class.java))
        }
        "Update video library" -> {
          val actionScanVideo = VideoLibrary.Scan()
          actionScanVideo.execute(HostManager.getInstance(this).connection, null, null)
        }
      }
    }
    adapter.data = listOf(
        "Host",
        "Remote control",
        "Update video library"
    )
    binding.recyclerView.adapter = adapter
    adapter.notifyDataSetChanged()
  }

}