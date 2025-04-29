package com.mygame.viewmodel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.GameModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Отвечает за связь между данными модели и визуальным представлением
 */
public class ViewModelManager {
    private GameModel gameModel;
    private Viewport viewport;
    private Box2DDebugRenderer debugRenderer;

    private List<ViewModel> viewModels;

    private BackgroundViewModel backgroundViewModel;
    private PlayerViewModel playerViewModel;
    private GroundViewModel groundViewModel;

    /**
     * Создание менеджера моделей представления
     * @param gameModel модель игры (данные)
     * @param viewport видовой экран
     */
    public ViewModelManager(GameModel gameModel, Viewport viewport) {
        this.gameModel = gameModel;
        this.viewport = viewport;

        // Создаем отладочный рендерер с включенными всеми флагами для лучшей видимости хитбоксов
        boolean drawBodies = true;       // Рисовать тела
        boolean drawJoints = true;       // Рисовать соединения
        boolean drawAABBs = true;        // Рисовать AABB (оси выравнивания)
        boolean drawInactiveBodies = true; // Рисовать неактивные тела
        boolean drawVelocities = true;   // Рисовать векторы скорости
        boolean drawContacts = true;     // Рисовать точки контакта
        this.debugRenderer = new Box2DDebugRenderer(
            drawBodies, drawJoints, drawAABBs,
            drawInactiveBodies, drawVelocities, drawContacts
        );

        viewModels = new ArrayList<>();

        // Создание моделей представления для каждого игрового объекта
        initViewModels();
    }

    /**
     * Инициализация моделей представления
     */
    private void initViewModels() {
        // Создаем модели представления и добавляем их в список
        backgroundViewModel = new BackgroundViewModel(viewport);
        viewModels.add(backgroundViewModel);

        groundViewModel = new GroundViewModel(gameModel, viewport);
        viewModels.add(groundViewModel);

        playerViewModel = new PlayerViewModel(gameModel.getPlayer());
        viewModels.add(playerViewModel);
    }

    /**
     * Обновление состояния всех моделей представления
     */
    public void update() {
        for (ViewModel viewModel : viewModels) {
            viewModel.update(viewport);
        }
    }

    /**
     * Получение всех моделей представления для отрисовки
     */
    public List<ViewModel> getViewModels() {
        return viewModels;
    }

    /**
     * Получение отладочного рендерера физики
     */
    public Box2DDebugRenderer getDebugRenderer() {
        return debugRenderer;
    }

    /**
     * Получение физического мира для отладочного рендеринга
     */
    public com.badlogic.gdx.physics.box2d.World getPhysicsWorld() {
        return gameModel.getPhysicsWorld();
    }

    /**
     * Освобождение ресурсов
     */
    public void dispose() {
        for (ViewModel viewModel : viewModels) {
            viewModel.dispose();
        }

        debugRenderer.dispose();
    }
}
