import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {axiosInstance} from "../../axios";
import {AppDispatch} from "../store";
import {IUser} from "../../models/IUser";

interface IUserReducerState {
    user: IUser | null
    isLoading: boolean,
    error: string
}

const initialState: IUserReducerState = {
    user: null,
    isLoading: false,
    error: ""
}

const userSlice = createSlice({
    name: "user",
    initialState,
    reducers: {
        authFetching(state) {
            state.isLoading = true;
        },
        authFetchingSuccess(state, action: PayloadAction<IUser>) {
            state.user = action.payload
            state.isLoading = false;
            state.error = "";
        },
        authFetchingError(state, action: PayloadAction<string>) {
            state.user = null;
            state.isLoading = false;
            state.error = action.payload;
        },
        addProduct(state, action: PayloadAction<any>) {
            if (state.user) state.user.products.push(action.payload);
        }
    },
});

export const authMe = () => async (dispatch: AppDispatch) => {
    try {
        dispatch(userSlice.actions.authFetching());
        const response = await axiosInstance.get<IUser>("/auth/me");
        dispatch(userSlice.actions.authFetchingSuccess(response.data));
    } catch (e: any) {
    }
}

export const auth = (email: string, password: string, rememberMe: boolean) => async (dispatch: AppDispatch) => {
    const requestBody = {
        email,
        password,
        rememberMe
    }
    try {
        dispatch(userSlice.actions.authFetching());
        const response = await axiosInstance.post<IUser>("/auth", requestBody);
        dispatch(userSlice.actions.authFetchingSuccess(response.data));
    } catch (e: any) {
        dispatch(userSlice.actions.authFetchingError(e.response.data.message));
    }
}

export const addProduct = (data: any) => async (dispatch: AppDispatch) => {
    try {
        const response = await axiosInstance.post("/products/add", data);
        dispatch(userSlice.actions.addProduct(response.data));
    } catch (e: any) {
    }
}

export default userSlice.reducer;