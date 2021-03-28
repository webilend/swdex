package it.webilend.swdex

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.vishal.weather.kotlin.network.SWAPIClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

import it.webilend.swdex.dummy.DummyContent
import it.webilend.swdex.model.Character
import it.webilend.swdex.model.SWAPIResponse
import it.webilend.swdex.network.SWRestService

class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private val swRestService: SWRestService =
        SWAPIClient.getClient().create(SWRestService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            sendEmailToDeveloper()
        }

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        swRestService.getCharacters()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { response ->
                response.results.forEach {char ->
                    char.id = char.url!!.substringBeforeLast("/").substringAfterLast("/")
                }
                response
            }
            .subscribe({response -> onResponse(response)}, {t -> onFailure(t) })
    }

    private fun onFailure(t: Throwable) {
        Toast.makeText(this,t.message, Toast.LENGTH_SHORT).show()
    }

    private fun onResponse(response: SWAPIResponse<Character>) {
        setupRecyclerView(findViewById(R.id.item_list), response.results)
        //progress_bar.visibility = View.GONE
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items:List<Character>) {
        recyclerView.adapter = ListItemRecyclerViewAdapter(this, items, twoPane)
    }

    private fun sendEmailToDeveloper() {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, MY_EMAIL);
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_email_subject));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent)
            }

    }

    class ListItemRecyclerViewAdapter(private val parentActivity: ItemListActivity,
                                      private val values: List<Character>,
                                      private val twoPane: Boolean) :
            RecyclerView.Adapter<ListItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.contentView.text = item.name
            Glide
                .with(holder.avatarView.context)
                .load(AVATAR_ENDPOINT.replace("{id}",item.id!!))
                .placeholder(R.drawable.loading_spinner)
                .error(R.drawable.placeholder)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(180)))
                .into(holder.avatarView);
            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val avatarView: ImageView = view.findViewById(R.id.avatar)
            val contentView: TextView = view.findViewById(R.id.content)
        }
    }
}