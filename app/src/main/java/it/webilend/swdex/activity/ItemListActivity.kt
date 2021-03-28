package it.webilend.swdex.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import it.webilend.swdex.*
import it.webilend.swdex.fragment.ItemDetailFragment
import it.webilend.swdex.model.Character
import it.webilend.swdex.model.SWAPIResponse


class ItemListActivity : AppCompatActivity() {
    private var twoPane: Boolean = false // for tablets
    private var page = 1
    private var mode = "LIST"
    private var characters = mutableListOf<Character>()


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
        findViewById<ProgressBar>(R.id.main_loading).visibility = View.VISIBLE
        SWManager.swRestService.getCharacters(page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { response ->
                response.results.forEach {char ->
                    char.id = char.url!!.substringBeforeLast("/").substringAfterLast("/")
                }
                SWManager.characters.addAll(response.results)
                this.characters = SWManager.characters
                page++
                response
            }
            .subscribe({response -> onResponse(response)}, {t -> onFailure(t) })
    }

    private fun onFailure(t: Throwable) {
        findViewById<ProgressBar>(R.id.main_loading).visibility = View.GONE
        Toast.makeText(this,t.message, Toast.LENGTH_SHORT).show()
    }

    private fun onResponse(response: SWAPIResponse<Character>) {
        setupRecyclerView(findViewById(R.id.item_list), response.results)
        findViewById<ProgressBar>(R.id.main_loading).visibility = View.GONE
    }

    private fun switchMode() {
        mode = if(this.mode == "LIST") "GRID" else "LIST"
        val recyclerView = findViewById<RecyclerView>(R.id.item_list)
        if(mode == "LIST") {
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
        else {
            recyclerView.layoutManager = GridLayoutManager(this, 3)
        }
        recyclerView.adapter =
            ListItemRecyclerViewAdapter(
                this,
                this.characters,
                mode
            )

    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items:List<Character>) {
        recyclerView.adapter =
            ListItemRecyclerViewAdapter(
                this,
                items,
                mode
            )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.switch_mode -> {
                switchMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                                      private val mode: String) :
            RecyclerView.Adapter<ListItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Character

                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                }
                v.context.startActivity(intent)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.containerView.orientation = if(mode == "LIST") LinearLayout.HORIZONTAL else LinearLayout.VERTICAL
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
            val containerView: LinearLayout = view.findViewById(R.id.list_container)
            val avatarView: ImageView = view.findViewById(R.id.avatar)
            val contentView: TextView = view.findViewById(R.id.content)
        }
    }
}