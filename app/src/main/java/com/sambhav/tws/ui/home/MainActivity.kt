package com.sambhav.tws.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityMainBinding
import com.sambhav.tws.ui.SplashActivity
import com.sambhav.tws.ui.grade.model.GradeData
import com.sambhav.tws.ui.home.classes.fragment.ClassFragment
import com.sambhav.tws.ui.home.doubt.fragment.DoubtFragment
import com.sambhav.tws.ui.home.home.HomeFragment
import com.sambhav.tws.ui.home.notes.fragments.NotesFragment
import com.sambhav.tws.ui.home.videos.fragment.VideosFragment
import com.sambhav.tws.ui.home.viewModel.HomeDataViewModel
import com.sambhav.tws.ui.payemnt.BasePaymentActivity
import com.sambhav.tws.ui.payemnt.PaymentActivity
import com.sambhav.tws.ui.profile.activities.StudentProfileActivity
import com.sambhav.tws.ui.profile.activities.TeacherProfileActivity
import com.sambhav.tws.ui.subject.model.SubjectData
import com.sambhav.tws.utils.CustomClickListener
import com.sambhav.tws.utils.getStudentData
import getAlertDailog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_back.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.koin.android.ext.android.get

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    public val viewModel: HomeDataViewModel = get()
    var mAllSubject = ArrayList<SubjectData>()
    var mAllGrade = MutableLiveData<ArrayList<GradeData>>()

    private var canBack = false
    private var fragments = ArrayList<Fragment>()
    private var previousPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = viewModel
        mBinding.executePendingBindings()
        observerData()
        loadFragment(HomeFragment())
        setBack(R.drawable.back_home)
        addBottomListener()
        onClickListener()
    }

    private fun observerData() {
        viewModel.allSubjects.observe(this, Observer {
            mAllSubject = it
        })
        viewModel.allGrade.observe(this, Observer {
            mAllGrade.postValue(it)
        })
        viewModel.logout.observe(this, Observer {
            toast(it.message)
            if (!it.error) {
                mPreference.isLogin = false
                startActivity(Intent(this, SplashActivity::class.java))
                finishAffinity()
            }
        })

        handleAuth(viewModel)
    }

    private fun addBottomListener() {
        bottomNavigation.itemIconTintList = null
        bottomNavigation.setOnNavigationItemReselectedListener { }
        bottomNavigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.menu_home -> {
                    setBack(R.drawable.back_home)
                    replaceFragment(HomeFragment())
                }

                R.id.menu_live -> {
                    if (mPreference.isStudent)
                    {
                        if (getStudentData(mPreference).is_premium == "0")
                            {
                                openPaymentActivity()
                            }

                    }
                    else {
                        setBack(R.drawable.back_class)
                        replaceFragment(ClassFragment())
                    }

                        /* if (mPreference.isStudent) {
                             if (getStudentData(mPreference).is_premium == "0" && getStudentData(
                                     mPreference
                                 ).school_id == "1"
                             ) {
                                 openPaymentActivity()
                             } else {
                                 setBack(R.drawable.back_class)
                                 replaceFragment(ClassFragment())
                             }
                         } else {
                             setBack(R.drawable.back_class)
                             replaceFragment(ClassFragment())
                         }*/
                }

                R.id.menu_video -> {
                    setBack(R.drawable.back_video)
                    replaceFragment(VideosFragment())
                }

                R.id.menu_notes -> {
                    setBack(R.drawable.back_notes)
                    replaceFragment(NotesFragment())
                }

                R.id.menu_doubt -> {
                    if (mPreference.isStudent) {
                        if (getStudentData(mPreference).is_premium == "0" && getStudentData(
                                mPreference
                            ).school_id == "1"
                        ) {
                            openPaymentActivity()
                        } else {
                            setBack(R.drawable.back_doubt)
                            replaceFragment(DoubtFragment())
                        }
                    } else {
                        setBack(R.drawable.back_doubt)
                        replaceFragment(DoubtFragment())
                    }
                }
            }
            true
        }
    }

    private fun onClickListener() {
        menu_drawer.setOnClickListener(CustomClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                mNavView.bringToFront()
                drawerLayout.openDrawer(GravityCompat.START)
            }
        })

        nav.menu_profile.setOnClickListener(CustomClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            val intent = if (mPreference.isStudent)
                Intent(this, StudentProfileActivity::class.java)
            else
                Intent(this, TeacherProfileActivity::class.java)
            startActivity(intent)
        })

        nav.menu_site_url.setOnClickListener(CustomClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(getString(R.string.app_site_link))
            startActivity(i)
        })

        nav.menu_logout.setOnClickListener(CustomClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            this.getAlertDailog("", "Are you sure you want to logout?", {
                drawerLayout.closeDrawer(GravityCompat.START)
                viewModel.logout()
            })
        })
    }

    private fun setBack(icon: Int) {
        rootLayout.setBackgroundResource(icon)
    }

    private fun loadFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, frag)
            .commit()
    }

    fun replaceFragment(frag: Fragment, id: Int = -1) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, frag)
            .commit()
        if (id > 0) {
            bottomNavigation.menu.findItem(id)?.isChecked = true
        }
    }

    override fun onBackPressed() {
        if (canBack) {
            super.onBackPressed()
        } else {
            canBack = true
            shoToast("Press again to exit!!")
        }

    }

    private fun shoToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    fun openPaymentActivity() {
        //startActivityForResult(Intent(this, PaymentActivity::class.java), 1000)
        startActivityForResult(Intent(this, BasePaymentActivity::class.java), 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            bottomNavigation.selectedItemId = R.id.menu_home
        }
    }
}
