import {configureStore, ThunkAction, Action, combineReducers} from '@reduxjs/toolkit';
import userReducer from "./reducers/userReducer";

const rootReducer = combineReducers({
    userReducer,
});

export const store = configureStore({
    reducer: rootReducer
});

// @ts-ignore
window.store = store.getState;

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<ReturnType, RootState, unknown, Action<string>>;
