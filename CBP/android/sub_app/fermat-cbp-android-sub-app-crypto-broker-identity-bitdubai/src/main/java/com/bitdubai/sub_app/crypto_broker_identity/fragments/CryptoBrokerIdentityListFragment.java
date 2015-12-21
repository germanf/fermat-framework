package com.bitdubai.sub_app.crypto_broker_identity.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.util.FermatDividerItemDecoration;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.exceptions.CantListCryptoBrokersException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.interfaces.CryptoBrokerIdentityInformation;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.interfaces.CryptoBrokerIdentityModuleManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.sub_app.crypto_broker_identity.R;
import com.bitdubai.sub_app.crypto_broker_identity.common.adapters.CryptoBrokerIdentityInfoAdapter;
import com.bitdubai.sub_app.crypto_broker_identity.session.CryptoBrokerIdentitySubAppSession;
import com.bitdubai.sub_app.crypto_broker_identity.util.CryptoBrokerIdentityListFilter;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.bitdubai.sub_app.crypto_broker_identity.session.CryptoBrokerIdentitySubAppSession.IDENTITY_INFO;

/**
 * A simple {@link Fragment} subclass.
 */
public class CryptoBrokerIdentityListFragment extends FermatListFragment<CryptoBrokerIdentityInformation>
        implements FermatListItemListeners<CryptoBrokerIdentityInformation>, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    // Constants
    private static final String TAG = "BrokerIdentityList";

    // Fermat Managers
    private CryptoBrokerIdentityModuleManager moduleManager;
    private ErrorManager errorManager;

    // Data
    private ArrayList<CryptoBrokerIdentityInformation> identityInformationList;

    // UI
    private View noMatchView;
    private CryptoBrokerIdentityListFilter filter;


    public static CryptoBrokerIdentityListFragment newInstance() {
        return new CryptoBrokerIdentityListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // setting up  module
            moduleManager = ((CryptoBrokerIdentitySubAppSession) appSession).getModuleManager();
            errorManager = appSession.getErrorManager();
            identityInformationList = (ArrayList) getMoreDataAsync(FermatRefreshTypes.NEW, 0);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);

            if (errorManager != null) {
                errorManager.reportUnexpectedSubAppException(
                        SubApps.CBP_CRYPTO_BROKER_IDENTITY,
                        UnexpectedSubAppExceptionSeverity.DISABLES_THIS_FRAGMENT,
                        ex);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.crypto_broker_identity_menu, menu);

//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        MenuItemCompat.setShowAsAction(searchItem, MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_ALWAYS);
//        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        mSearchView.setOnQueryTextListener(this);
//        mSearchView.setOnCloseListener(this);
    }

    @Override
    protected void initViews(View layout) {
        super.initViews(layout);

        FloatingActionButton newIdentityButton = (FloatingActionButton) layout.findViewById(R.id.new_crypto_broker_identity_float_action_button);
        newIdentityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(Activities.CBP_SUB_APP_CRYPTO_BROKER_IDENTITY_CREATE_IDENTITY.getCode(), appSession.getAppPublicKey());
            }
        });

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        }

        noMatchView = layout.findViewById(R.id.no_matches_crypto_broker_identity);

        RecyclerView.ItemDecoration itemDecoration = new FermatDividerItemDecoration(getActivity(), R.drawable.divider_shape);
        recyclerView.addItemDecoration(itemDecoration);

        if (identityInformationList == null || identityInformationList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            View emptyListViewsContainer = layout.findViewById(R.id.no_crypto_broker_identities);
            emptyListViewsContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean hasMenu() {
        return true;
    }

    @Override
    public FermatAdapter getAdapter() {
        if (adapter == null) {
            adapter = new CryptoBrokerIdentityInfoAdapter(getActivity(), identityInformationList);
            adapter.setFermatListEventListener(this); // setting up event listeners
        }
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        return layoutManager;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_crypto_broker_identity_list;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.crypto_broker_identity_recycler_view;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh;
    }

    @Override
    public List<CryptoBrokerIdentityInformation> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {
        List<CryptoBrokerIdentityInformation> data = new ArrayList<>();

        try {
            data = moduleManager.listIdentities(0, 0);
        } catch (CantListCryptoBrokersException ex) {
            errorManager.reportUnexpectedSubAppException(
                    SubApps.CBP_CRYPTO_BROKER_IDENTITY,
                    UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                    ex);
        }

        return data;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }

    @Override
    public void onItemClickListener(CryptoBrokerIdentityInformation data, int position) {
        appSession.setData(IDENTITY_INFO, data);
        changeActivity(Activities.CBP_SUB_APP_CRYPTO_BROKER_IDENTITY_EDIT_IDENTITY.getCode(), appSession.getAppPublicKey());
    }

    @Override
    public void onLongItemClickListener(CryptoBrokerIdentityInformation data, int position) {

    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                identityInformationList = (ArrayList) result[0];
                if (adapter != null)
                    adapter.changeDataSet(identityInformationList);
            }
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String text) {
        if (filter == null) {
            CryptoBrokerIdentityInfoAdapter infoAdapter = (CryptoBrokerIdentityInfoAdapter) this.adapter;

            filter = (CryptoBrokerIdentityListFilter) infoAdapter.getFilter();
            filter.setNoMatchViews(noMatchView, recyclerView);
        }

        filter.filter(text);

        return true;
    }
}
