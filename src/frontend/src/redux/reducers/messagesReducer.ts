import {createSlice} from "@reduxjs/toolkit";
import {IUser} from "../../models/IUser";

interface Imessage {
    user: IUser | null
    isLoading: boolean,
    error: string
}

const messagesReducer = createSlice({
    name: "messages",
    initialState: {},
    reducers: {
        receiveMessage() {

        },
    }
})