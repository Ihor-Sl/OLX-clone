import React, {FC, useEffect} from "react";
import Header from "./Header";
import Products from "./Products";
import {Navigate, Route, Routes} from "react-router-dom";
import ProductPage from "./ProductPage";
import Profile from "./Profile";
import {useAppDispatch, useAppSelector} from "../redux/hooks";
import LoginForm from "./LoginForm";
import {authMe} from "../redux/reducers/userReducer";
import ProtectedRoutes from "./ProtectedRoutes";
import NewProduct from "./NewProduct";

const ws = new WebSocket("ws://localhost:8080/root");

const App:FC = () => {

    const user = useAppSelector((state) => state.userReducer.user);
    const dispatch = useAppDispatch();

    useEffect(() => {
        dispatch(authMe());
    }, []);

    if (user) {
        ws.addEventListener("message", (e) => {
            console.log(e.data)
        });
        // ws.send(JSON.stringify({body: Hello bro can I buy this?", productId: 1}))
    }

    return (
        <>
            <Header/>
            <Routes>
                <Route path="/" element={<Products/>}/>
                <Route path="/login" element={<LoginForm/>}/>
                <Route path="/products/id/:id" element={<ProductPage/>}/>
                <Route path="/profile/id/:id" element={<Profile/>}/>
                <Route path="*" element={<Navigate to="/"/>}/>
                <Route element={<ProtectedRoutes/>}>
                    <Route path="/profile/me" element={<Profile/>}/>
                    <Route path="/products/add" element={<NewProduct/>}/>
                </Route>
            </Routes>
        </>
    )
};

export default App;
