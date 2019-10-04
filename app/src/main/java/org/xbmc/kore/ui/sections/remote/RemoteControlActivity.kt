package org.xbmc.kore.ui.sections.remote

import android.databinding.DataBindingUtil
import android.os.Bundle
import org.xbmc.kore.R
import org.xbmc.kore.databinding.ActivityRemoteControlerBinding
import org.xbmc.kore.ui.BaseActivity

class RemoteControlActivity : BaseActivity() {

  private lateinit var binding: ActivityRemoteControlerBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_controler)
    supportFragmentManager.beginTransaction()
        .replace(R.id.frame_layout, RemoteFragment())
        .commit()
  }

}