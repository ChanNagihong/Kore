package org.xbmc.kore.ui.sections.remote

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.xbmc.kore.R

class MySettingsAdapter(private val onItemClick: ((position: Int, text: String?) -> Unit)) :
    RecyclerView.Adapter<MySettingsViewHolder>() {

  var data: List<String>? = null

  override fun onCreateViewHolder(
    p0: ViewGroup,
    p1: Int
  ): MySettingsViewHolder {
    val view = LayoutInflater.from(p0.context)
        .inflate(R.layout.activity_my_settings_item, p0, false)
    return MySettingsViewHolder(view)
  }

  override fun getItemCount(): Int {
    return data?.size ?: 0
  }

  override fun onBindViewHolder(
    p0: MySettingsViewHolder,
    p1: Int
  ) {
    p0.onBind(data?.get(p1), onItemClick)
  }

}