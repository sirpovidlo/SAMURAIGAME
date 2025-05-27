package com.mygame.viewmodel;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.events.*;
import com.mygame.model.GameModel;
import com.mygame.model.GameObject;
import com.mygame.model.Player;

import java.util.*;

/**
 * Отвечает за связь между данными модели и визуальным представлением
 */
public class ViewModelManager implements GameEventListener {
    private final GameModel gameModel;
    private final Viewport viewport;
    private final Box2DDebugRenderer debugRenderer;
    private final Map<String, ViewModel> viewModels;
    private final Map<String, String> gameObjectToViewModelMap; // Карта соответствий ID
    private final Map<Class<? extends GameObject>, ViewModelFactory> viewModelFactories;

    private BackgroundViewModel backgroundViewModel;
    private PlayerViewModel playerViewModel;
    private GroundViewModel groundViewModel;

    /**
     * Создание менеджера моделей представления
     */
    public ViewModelManager(GameModel gameModel, Viewport viewport) {
        this.gameModel = gameModel;
        this.viewport = viewport;
        this.viewModels = new HashMap<>();
        this.gameObjectToViewModelMap = new HashMap<>();
        this.viewModelFactories = new HashMap<>();

        // Создаем отладочный рендерер
        this.debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

        // Регистрируем фабрики для разных типов игровых объектов
        registerViewModelFactories();

        // Подписываемся на события
        GameEventBus.getInstance().subscribe(GameEventType.OBJECT_CREATED, this);
        GameEventBus.getInstance().subscribe(GameEventType.OBJECT_DELETED, this);

        // Создание моделей представления для каждого игрового объекта
        initViewModels();
    }

    /**
     * Регистрация фабрик для создания ViewModel
     */
    private void registerViewModelFactories() {
        viewModelFactories.put(Player.class, new PlayerViewModelFactory());
        // Добавить другие фабрики по мере необходимости
    }

    /**
     * Обработка игровых событий
     */
    @Override
    public void onEvent(GameEvent event) {
        if (event instanceof GameObjectEvent) {
            GameObjectEvent objectEvent = (GameObjectEvent) event;
            GameObject gameObject = objectEvent.getGameObject();

            switch (event.getType()) {
                case OBJECT_CREATED:
                    createViewModel(gameObject);
                    break;
                case OBJECT_DELETED:
                    removeViewModelForGameObject(gameObject.getId());
                    break;
            }
        }
    }

    /**
     * Создание ViewModel для игрового объекта
     */
    private void createViewModel(GameObject gameObject) {
        ViewModelFactory factory = viewModelFactories.get(gameObject.getClass());
        if (factory != null) {
            ViewModel viewModel = factory.create(gameObject.getId());
            String viewModelId = viewModel.getGameObjectId();
            viewModels.put(viewModelId, viewModel);
            gameObjectToViewModelMap.put(gameObject.getId(), viewModelId);
        }
    }

    /**
     * Удаление ViewModel для игрового объекта
     */
    private void removeViewModelForGameObject(String gameObjectId) {
        String viewModelId = gameObjectToViewModelMap.remove(gameObjectId);
        if (viewModelId != null) {
            ViewModel viewModel = viewModels.remove(viewModelId);
            if (viewModel != null) {
                viewModel.dispose();
            }
        }
    }

    /**
     * Инициализация моделей представления
     */
    private void initViewModels() {
        // Создаем модели представления и добавляем их в список
        backgroundViewModel = new BackgroundViewModel(viewport);
        String backgroundId = backgroundViewModel.getGameObjectId();
        viewModels.put(backgroundId, backgroundViewModel);

        groundViewModel = new GroundViewModel(gameModel, viewport);
        String groundId = groundViewModel.getGameObjectId();
        viewModels.put(groundId, groundViewModel);

        // Создаем ViewModel для игрока
        Player player = gameModel.getPlayer();
        playerViewModel = new PlayerViewModel(player.getId());
        String playerViewModelId = playerViewModel.getGameObjectId();
        viewModels.put(playerViewModelId, playerViewModel);
        gameObjectToViewModelMap.put(player.getId(), playerViewModelId);
    }

    /**
     * Обновление всех моделей представления
     */
    public void update() {
        for (GameObject gameObject : gameModel.getGameObjects()) {
            String viewModelId = gameObjectToViewModelMap.get(gameObject.getId());
            if (viewModelId != null) {
                ViewModel viewModel = viewModels.get(viewModelId);
                if (viewModel != null) {
                    viewModel.update(gameObject, viewport);
                }
            }
        }
    }

    /**
     * Получение всех моделей представления
     */
    public Collection<ViewModel> getViewModels() {
        return viewModels.values();
    }

    public Box2DDebugRenderer getDebugRenderer() {
        return debugRenderer;
    }

    public com.badlogic.gdx.physics.box2d.World getPhysicsWorld() {
        return gameModel.getPhysicsWorld();
    }

    /**
     * Освобождение ресурсов
     */
    public void dispose() {
        for (ViewModel viewModel : viewModels.values()) {
            viewModel.dispose();
        }
        viewModels.clear();
        gameObjectToViewModelMap.clear();
        debugRenderer.dispose();

        // Отписываемся от событий
        GameEventBus.getInstance().unsubscribe(GameEventType.OBJECT_CREATED, this);
        GameEventBus.getInstance().unsubscribe(GameEventType.OBJECT_DELETED, this);
    }
}
