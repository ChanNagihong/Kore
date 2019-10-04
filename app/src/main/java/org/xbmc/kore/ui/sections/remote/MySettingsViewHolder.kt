package org.xbmc.kore.ui.sections.remote

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import org.xbmc.kore.databinding.ActivityMySettingsItemBinding

class MySettingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

  fun onBind(text: String?, onItemClick: ((position: Int, text: String?) -> Unit)) {
    val binding = DataBindingUtil.bind<ActivityMySettingsItemBinding>(itemView) ?: return
    binding.text.text = text
    binding.text.setOnClickListener {
      onItemClick.invoke(adapterPosition, (it as? TextView)?.text?.toString())
    }
  }

}