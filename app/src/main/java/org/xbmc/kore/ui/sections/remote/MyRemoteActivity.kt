package org.xbmc.kore.ui.sections.remote

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout.OnTabSelectedListener
import android.support.design.widget.TabLayout.Tab
import android.support.v4.text.TextDirectionHeuristicsCompat
import android.support.v7.preference.PreferenceManager
import android.util.Log
import org.xbmc.kore.R
import org.xbmc.kore.Settings
import org.xbmc.kore.databinding.ActivityMyRemoteBinding
import org.xbmc.kore.host.HostConnectionObserver
import org.xbmc.kore.host.HostManager
import org.xbmc.kore.jsonrpc.method.Files.Media
import org.xbmc.kore.jsonrpc.method.Input
import org.xbmc.kore.jsonrpc.notification.Player.NotificationsData
import org.xbmc.kore.jsonrpc.type.ListType
import org.xbmc.kore.jsonrpc.type.ListType.ItemsAll
import org.xbmc.kore.jsonrpc.type.PlayerType.GetActivePlayersReturnType
import org.xbmc.kore.jsonrpc.type.PlayerType.PropertyValue
import org.xbmc.kore.service.ConnectionObserversManagerService
import org.xbmc.kore.ui.BaseActivity
import org.xbmc.kore.ui.generic.SendTextDialogFragment
import org.xbmc.kore.ui.sections.file.MediaFileListFragment
import org.xbmc.kore.ui.sections.hosts.AddHostActivity
import org.xbmc.kore.utils.LogUtils
import org.xbmc.kore.utils.TabsAdapter
import org.xbmc.kore.utils.Utils

class MyRemoteActivity : BaseActivity(), HostConnectionObserver.PlayerEventsObserver,
    NowPlayingFragment.NowPlayingListener, SendTextDialogFragment.SendTextDialogListener {

  private val TAG = LogUtils.makeLogTag(RemoteActivity::class.java)

  private lateinit var binding: ActivityMyRemoteBinding
  private lateinit var hostManager: HostManager
  private lateinit var tabsAdapter: TabsAdapter

  private var hostConnectionObserver: HostConnectionObserver? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_my_remote)
    hostManager = HostManager.getInstance(this)

    if (hostManager.hostInfo == null) {
      val intent = Intent(this, AddHostActivity::class.java)
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      startActivity(intent)
      finish()
      return
    }

    val moviesBundle = Bundle()
    moviesBundle.putString(MediaFileListFragment.MEDIA_TYPE, Media.VIDEO)
    moviesBundle.putParcelable(
        MediaFileListFragment.SORT_METHOD, ListType.Sort(ListType.Sort.SORT_METHOD_PATH, true, true)
    )
    moviesBundle.putString(
        MediaFileListFragment.CUSTOM_PATH,
        "multipath://%2fhome%2fnagihong%2fhdd%2fmovies%2f/%2fhome%2fnagihong%2fMovies%2f/"
    )

    val tvBundle = Bundle()
    tvBundle.putString(MediaFileListFragment.MEDIA_TYPE, Media.VIDEO)
    tvBundle.putParcelable(
        MediaFileListFragment.SORT_METHOD, ListType.Sort(ListType.Sort.SORT_METHOD_PATH, true, true)
    )
    tvBundle.putString(
        MediaFileListFragment.CUSTOM_PATH,
        "multipath://%2fhome%2fnagihong%2fTv%2f/%2fhome%2fnagihong%2fhdd%2ftv%2f/"
    )

    val variousBundle = Bundle()
    variousBundle.putString(MediaFileListFragment.MEDIA_TYPE, Media.VIDEO)
    variousBundle.putParcelable(
        MediaFileListFragment.SORT_METHOD, ListType.Sort(ListType.Sort.SORT_METHOD_PATH, true, true)
    )
    variousBundle.putString(
        MediaFileListFragment.CUSTOM_PATH,
        "multipath://%2fhome%2fnagihong%2fVarious%2f/%2fhome%2fnagihong%2fhdd%2fvarious%2f/"
    )

    tabsAdapter = TabsAdapter(this, supportFragmentManager)
        .addTab(NowPlayingFragment::class.java, null, "正在播", 1)
        .addTab(MediaFileListFragment::class.java, moviesBundle, "电影", 2)
        .addTab(MediaFileListFragment::class.java, tvBundle, "电视", 3)
        .addTab(MediaFileListFragment::class.java, variousBundle, "综艺", 4)

    binding.viewPager.offscreenPageLimit = 4
    binding.viewPager.adapter = tabsAdapter
    binding.tabLayout.setupWithViewPager(binding.viewPager)
    binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {

      private val INTERVAL = 1000L
      private var lastClickTime = 0L
      private var clickTimes = 0

      override fun onTabReselected(p0: Tab) {
        if (p0.position != 0) return
        val time = System.currentTimeMillis()
        if (time - lastClickTime < INTERVAL) {
          clickTimes++
          if (clickTimes == 5) {
            clickTimes = 0
            startActivity(Intent(this@MyRemoteActivity, MySettingsActivity::class.java))
          }
        } else {
          clickTimes = 1
        }
        lastClickTime = time
      }

      override fun onTabUnselected(p0: Tab?) {

      }

      override fun onTabSelected(p0: Tab) {

      }

    })
    tabsAdapter.notifyDataSetChanged()
  }

  override fun onResume() {
    super.onResume()
    hostConnectionObserver = hostManager.hostConnectionObserver
    hostConnectionObserver?.registerPlayerObserver(this)
    hostConnectionObserver?.refreshWhatsPlaying()
    hostConnectionObserver?.refreshPlaylists()
  }

  override fun onPause() {
    super.onPause()
    hostConnectionObserver?.unregisterPlayerObserver(this)
    hostConnectionObserver = null
  }

  // <editor-fold desc="SendTextDialogFragment.SendTextDialogListener">    ----------------------------------------------------------
  override fun onSendTextFinished(
    text: String,
    done: Boolean
  ) {
    val sendText =
      if (TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR.isRtl(text, 0, text.length)) {
        StringBuilder(text).reverse()
            .toString()
      } else text
    val action = Input.SendText(sendText, done)
    action.execute(hostManager.connection, null, null)
  }

  override fun onSendTextCancel() {

  }
  // </editor-fold desc="SendTextDialogFragment.SendTextDialogListener">    ---------------------------------------------------------

  // <editor-fold desc="NowPlayingFragment.NowPlayingListener">    ----------------------------------------------------------
  override fun SwitchToRemotePanel() {
    binding.viewPager.currentItem = 0
  }
  // </editor-fold desc="NowPlayingFragment.NowPlayingListener">    ---------------------------------------------------------

  // <editor-fold desc="HostConnectionObserver.PlayerEventsObserver">    ----------------------------------------------------------
  override fun playerOnPropertyChanged(notificationsData: NotificationsData?) {

  }

  override fun playerOnPlay(
    getActivePlayerResult: GetActivePlayersReturnType?,
    getPropertiesResult: PropertyValue?,
    getItemResult: ItemsAll?
  ) {
    SwitchToRemotePanel()

    val showNotification = PreferenceManager
        .getDefaultSharedPreferences(this)
        .getBoolean(
            Settings.KEY_PREF_SHOW_NOTIFICATION,
            Settings.DEFAULT_PREF_SHOW_NOTIFICATION
        )
    if (showNotification) {
      // Start service that manages connection observers
      LogUtils.LOGD(TAG, "Starting observer service")
      if (Utils.isOreoOrLater()) {
        startForegroundService(Intent(this, ConnectionObserversManagerService::class.java))
      } else {
        startService(Intent(this, ConnectionObserversManagerService::class.java))
      }
    }
  }

  override fun playerOnPause(
    getActivePlayerResult: GetActivePlayersReturnType?,
    getPropertiesResult: PropertyValue?,
    getItemResult: ItemsAll?
  ) {
    playerOnPlay(getActivePlayerResult, getPropertiesResult, getItemResult)
  }

  override fun playerOnStop() {

  }

  override fun playerOnConnectionError(
    errorCode: Int,
    description: String?
  ) {
    playerOnStop()
  }

  override fun playerNoResultsYet() {

  }

  override fun systemOnQuit() {
    playerOnStop()
  }

  override fun inputOnInputRequested(
    title: String?,
    type: String?,
    value: String?
  ) {
    val dialog = SendTextDialogFragment.newInstance(title)
    dialog.show(supportFragmentManager, null)
  }

  override fun observerOnStopObserving() {
  }
  // </editor-fold desc="HostConnectionObserver.PlayerEventsObserver">    ---------------------------------------------------------

}