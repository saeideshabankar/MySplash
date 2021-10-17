package com.example.mysplash.ui.main.details_frag

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.downloader.OnDownloadListener
import com.downloader.OnProgressListener
import com.downloader.PRDownloader
import com.downloader.Progress
import com.example.mysplash.R
import com.example.mysplash.data.models.photo_model.DetailsPhotoModel
import com.example.mysplash.databinding.FragmentDetailsBinding
import com.example.mysplash.utils.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_details.*
import nouri.`in`.goodprefslib.GoodPrefs


class DetailsFragment() : Fragment(), DetailsContract.View {
    private var urlCoverPhoto: String=""
    private var pageFlag: String=""
    private var imageName: String=""
    private var lightOrDark: String=""
    private var photoId: String=""
    private var profileUrlImg: String=""
    private val REQUEST_PERMISSION = 1
    private lateinit var binding: FragmentDetailsBinding
    private val presenter: DetailsPresenterImpl by lazy { DetailsPresenterImpl(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        }

         photoId = arguments?.getString(PHOTO_ID).toString()
         profileUrlImg = arguments?.getString(PROFILE_URL_IMG).toString()
        pageFlag = arguments?.getString(BACK_PAGE_FLAG).toString()


        Glide.with(requireActivity())
            .load(profileUrlImg)
            .into(binding.detailsProfile)

        presenter.callDetailsApiList(photoId, ACCESS_API_KEY)
        //if theme is dark icon tint must be changed
        if (GoodPrefs.getInstance().isKeyExists(PREFS_IS_LIGHT_OR_DARK)) {

            lightOrDark = GoodPrefs.getInstance().getString(PREFS_IS_LIGHT_OR_DARK, "")
            if (lightOrDark == "Light") {
                binding.detailsDownloadImg.setColorFilter(resources.getColor(R.color.black))
                binding.detailsLikeImg.setColorFilter(resources.getColor(R.color.black))
                binding.detailsSaveImg.setColorFilter(resources.getColor(R.color.black))
                binding.detailsFirstView.setBackgroundColor(resources.getColor(R.color.dark_gray))
                binding.detailsSecondView.setBackgroundColor(resources.getColor(R.color.dark_gray))
                binding.detailsThirdView.setBackgroundColor(resources.getColor(R.color.dark_gray))
            } else {
                binding.detailsDownloadImg.setColorFilter(resources.getColor(R.color.white))
                binding.detailsLikeImg.setColorFilter(resources.getColor(R.color.white))
                binding.detailsSaveImg.setColorFilter(resources.getColor(R.color.white))
                binding.detailsFirstView.setBackgroundColor(resources.getColor(R.color.white))
                binding.detailsSecondView.setBackgroundColor(resources.getColor(R.color.white))
                binding.detailsThirdView.setBackgroundColor(resources.getColor(R.color.white))
            }
        } else {
            Toast.makeText(requireContext(), "theme doesnt set", Toast.LENGTH_SHORT).show()
        }

        //DownloadImage
        binding.detailsDownloadImg.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(

                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        REQUEST_PERMISSION
                    )

                }
                //else they are granted
                else {
                    downloadImgByPrDownloader()
                }
            }
            //if version sdk code is lower than 6
            else {
                downloadImgByPrDownloader()

            }
        }
        binding.detailsBack1.setOnClickListener {

            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            if (pageFlag == ONE_COLLECTION_FRAG) {

                activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
                    View.VISIBLE
                activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility =
                    View.GONE
                activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility =
                    View.GONE
                activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility =
                    View.GONE

            } else if (pageFlag == HOME_FRAG) {

                activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
                    View.GONE
                activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility =
                    View.VISIBLE
                activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility =
                    View.VISIBLE
                activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility =
                    View.VISIBLE

            } else if (pageFlag == SEARCH_PHOTO_FRAG) {
                activity?.findViewById<ConstraintLayout>(R.id.Search_const)?.visibility =
                    View.VISIBLE
                activity?.findViewById<FragmentContainerView>(R.id.search_fragmentContainerView)?.visibility =
                    View.GONE

            } else if (pageFlag == SEARCH_COLLECTION_FRAG) {

                activity?.findViewById<ConstraintLayout>(R.id.Search_const)?.visibility =
                    View.GONE
                activity?.findViewById<ConstraintLayout>(R.id.search_fragmentContainerView)?.visibility =
                    View.VISIBLE
            }

            requireActivity().onBackPressed()

        }
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        binding.detailsFragCoverImg.setOnClickListener {
            dialog.setContentView(R.layout.dialog_show_img)

            val imgCloseDialogView = dialog.findViewById(R.id.dialog_close_img) as ImageView
            val imgDialogView = dialog.findViewById(R.id.dialog_img) as ImageView

            Glide.with(requireActivity())
                .load(urlCoverPhoto)
                .into(imgDialogView)

            imgCloseDialogView.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        //Set image as wallpaper
        binding.detailsSetWallpaperCons.setOnClickListener {
            val pb = ProgressDialog(requireContext())
            pb.setMessage("Setting as Wallpaper...")
            pb.setCancelable(false)
            pb.show()
            Glide.with(this)
                .asBitmap()
                .load(urlCoverPhoto)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        Log.i("TAGE", "onLoadFailed: ${errorDrawable.toString()}")
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val wallpaperManager = WallpaperManager.getInstance(requireContext())
                        wallpaperManager?.setBitmap(resource)
                        pb.setMessage("Your Wallpaper changed is completed")
                        pb.setCancelable(false)
                        pb.cancel()

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {


            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    requireContext(),
                    " Permission has been denied by user ${grantResults[0]}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    REQUEST_PERMISSION
                )

            } else {
                Toast.makeText(
                    requireContext(),
                    " Permission has garanted by user ${grantResults[0]}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                downloadImgByPrDownloader()
            }

        }
    }

    override fun loadDetailsApiList(data: DetailsPhotoModel) {
        urlCoverPhoto = data.urls.regular
        imageName = data.user.username

       if (this.isAdded) {
            binding.detailsFragCoverImg.showImageGlide(
                requireContext(),
                urlCoverPhoto,
                details_progressItem
            )
       }

        binding.detailsUsername.text = data.user.username
        binding.detailsResultCameraTv.text = data.exif.model
        binding.detailsApertureResultTv.text = data.exif.aperture
        binding.detailsFocalLengthResultTv.text = data.exif.focalLength
        //  String.format("%.3f", data.exif.exposureTime)
        binding.detailsShutterResultTv.text = data.exif.exposureTime
        binding.detailsIsoResultTv.text = data.exif.iso.toString()
        binding.detailsDimentionsResultTv.text = data.width.toString() + "+" + data.height
        binding.detailsResultLikesTv.text = data.likes.toString()
        binding.detailsDimentionsResultTv.text = data.downloads.toString()
    }
    override fun checkNetWorkConnection(): Boolean {
        return requireContext().isNetworkAvailable()
    }

    override fun errorNetWorkConnection() {

        Toast.makeText(requireContext(), "Error network connection", Toast.LENGTH_SHORT).show()
    }

    override fun responseCodeError() {

    }

    override fun responseError(error: Throwable) {

    }

    override fun serverError(error: String) {

        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun showLoader() {
        Handler().postDelayed({
            binding.detailsFragProgress.visibility = View.VISIBLE
        }, DETAILS_HANDLER)
    }

    override fun hideLoader() {
        Handler().postDelayed({
        binding.detailsFragProgress.visibility = View.INVISIBLE
        }, DETAILS_HANDLER)
    }

    private fun downloadImgByPrDownloader() {
        val pb = ProgressDialog(requireContext())
        pb.setMessage("Downloading...")
        pb.setCancelable(true)
        pb.show()
        val file1 =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        imageName = "$imageName.jpg"
        //in warning ro taghir nade chon in string ro bayad dar ghalebe str befreste
        PRDownloader.download(
            urlCoverPhoto,
            file1?.path, "$imageName"
        )
            .build()
            .setOnStartOrResumeListener { }
            .setOnPauseListener { }
            .setOnProgressListener {
                object : OnProgressListener {
                    override fun onProgress(progress: Progress?) {

                        val percentOfProgressDetails =
                            (100 / progress!!.totalBytes).also { progress.currentBytes = it }
                        pb.setMessage("Downloading : $percentOfProgressDetails %")
                    }
                }
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(requireContext(), "Download completed", Toast.LENGTH_SHORT)
                        .show()
                    pb.dismiss()
                    Log.e("TAGerror1", "completed")
                }

                override fun onError(error: com.downloader.Error?) {
                    Toast.makeText(
                        requireContext(),
                        "Download has error $error",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.e(
                        "TAGerror",
                        "onErrorconnection: ${error?.isConnectionError} + server error ${error?.isServerError}"
                    )
                }

                fun onError(error: Error?) {
                }
            })
    }


    override fun onDestroy() {
        super.onDestroy()
        //DARVAGHE INJA COLECT FRAG RO NESHONE GEREFTIM BARAY ONECOLLECTFRAG

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        if (pageFlag == ONE_COLLECTION_FRAG) {

            activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
                View.VISIBLE
            activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility =
                View.GONE
            activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility =
                View.GONE
            activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility =
                View.GONE

        } else if (pageFlag == HOME_FRAG) {

            activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
                View.GONE
            activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility =
                View.VISIBLE
            activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility =
                View.VISIBLE
            activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility =
                View.VISIBLE

        } else if (pageFlag == SEARCH_PHOTO_FRAG) {
            activity?.findViewById<ConstraintLayout>(R.id.Search_const)?.visibility =
                View.VISIBLE
            activity?.findViewById<FragmentContainerView>(R.id.search_fragmentContainerView)?.visibility =
                View.GONE

        } else if (pageFlag == SEARCH_COLLECTION_FRAG) {

            activity?.findViewById<ConstraintLayout>(R.id.Search_const)?.visibility =
                View.GONE
            activity?.findViewById<FragmentContainerView>(R.id.search_fragmentContainerView)?.visibility =
                View.VISIBLE
        }
    }
}
