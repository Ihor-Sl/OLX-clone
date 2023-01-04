import React from 'react';
import {useAppSelector} from "../redux/hooks";
import {Outlet} from "react-router-dom";
import LoginForm from "./LoginForm";

const ProtectedRoutes = () => {
    const user = useAppSelector(state => state.userReducer.user);
    return user ? <Outlet/> : <LoginForm/>
};

export default ProtectedRoutes;