package com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.interfaces.MatchingEngineManager;
import com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.database.MatchingEngineMiddlewareDao;
import com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.database.MatchingEngineMiddlewareDeveloperDatabaseFactory;
import com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.exceptions.CantInitializeDatabaseException;
import com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.structure.MatchingEngineMiddlewareManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import java.util.List;

/**
 * Created by Leon Acosta (lnacosta) - (laion.cj91@gmail.com) on 16/01/2015.
 *
 * @author lnacosta
 * @version 1.0
 */
public class MatchingEngineMiddlewarePluginRoot extends AbstractPlugin implements DatabaseManagerForDevelopers {

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM     , layer = Layers.PLATFORM_SERVICE, addon  = Addons.ERROR_MANAGER         )
    private ErrorManager errorManager;

    @NeededAddonReference (platform = Platforms.PLUG_INS_PLATFORM     , layer = Layers.PLATFORM_SERVICE, addon  = Addons .EVENT_MANAGER         )
    private EventManager eventManager;

    @NeededAddonReference (platform = Platforms.OPERATIVE_SYSTEM_API  , layer = Layers.SYSTEM          , addon  = Addons .PLUGIN_FILE_SYSTEM    )
    protected PluginFileSystem pluginFileSystem        ;

    @NeededAddonReference (platform = Platforms.OPERATIVE_SYSTEM_API  , layer = Layers.SYSTEM          , addon  = Addons .PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;


    public MatchingEngineMiddlewarePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    private MatchingEngineManager fermatManager;

    @Override
    public FermatManager getManager() {
        return fermatManager;
    }

    @Override
    public void start() throws CantStartPluginException {

        try {

            final MatchingEngineMiddlewareDao dao = new MatchingEngineMiddlewareDao(
                    pluginDatabaseSystem,
                    pluginId
            );

            dao.initialize();

            fermatManager = new MatchingEngineMiddlewareManager(
                    dao,
                    errorManager,
                    this.getPluginVersionReference()
            );

            super.start();

        } catch (final CantInitializeDatabaseException cantInitializeActorConnectionDatabaseException) {

            errorManager.reportUnexpectedPluginException(
                    getPluginVersionReference()                           ,
                    UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                    cantInitializeActorConnectionDatabaseException
            );

            throw new CantStartPluginException(
                    cantInitializeActorConnectionDatabaseException,
                    "Matching Engine Middleware.",
                    "Problem initializing database of the plug-in."
            );
        }
    }

    @Override
    public List<DeveloperDatabase> getDatabaseList(final DeveloperObjectFactory developerObjectFactory) {

        return new MatchingEngineMiddlewareDeveloperDatabaseFactory(
                pluginDatabaseSystem,
                pluginId
        ).getDatabaseList(
                developerObjectFactory
        );

    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(final DeveloperObjectFactory developerObjectFactory,
                                                             final DeveloperDatabase      developerDatabase     ) {

        return new MatchingEngineMiddlewareDeveloperDatabaseFactory(
                pluginDatabaseSystem,
                pluginId
        ).getDatabaseTableList(
                developerObjectFactory
        );
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(final DeveloperObjectFactory developerObjectFactory,
                                                                      final DeveloperDatabase      developerDatabase     ,
                                                                      final DeveloperDatabaseTable developerDatabaseTable) {

        return new MatchingEngineMiddlewareDeveloperDatabaseFactory(
                pluginDatabaseSystem,
                pluginId
        ).getDatabaseTableContent(
                developerObjectFactory,
                developerDatabaseTable
        );
    }

}