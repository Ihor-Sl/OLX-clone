import React, {FC, useState} from "react";
import {auth} from "../redux/reducers/userReducer";
import {useAppDispatch, useAppSelector} from "../redux/hooks";
import {Navigate} from "react-router-dom";

const LoginForm: FC = () => {

    const {user, error} = useAppSelector(state => state.userReducer);
    const dispatch = useAppDispatch();

    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [rememberMe, setRememberMe] = useState<boolean>(false);

    const submitForm = (e: React.MouseEvent) => {
        dispatch(auth(email, password, rememberMe));
        e.preventDefault();
    }

    if (user) {
        return <Navigate to={"/"} />
    }

    return (
        <form className="m-auto mt-5" style={{width: "300px"}}>
            <h1 className="h3 mb-3 fw-normal">Please sign in</h1>
            {error &&
                <div className="alert alert-danger" role="alert">
                    {error}
                </div>
            }
            <div className="form-floating mb-3">
                <input onChange={(e: React.ChangeEvent<HTMLInputElement>) => setEmail(e.target.value)}
                       value={email}
                       type="email" className="form-control" id="floatingInput" placeholder="name@example.com"/>
                <label htmlFor="floatingInput">Email address</label>
            </div>
            <div className="form-floating mb-3">
                <input onChange={(e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)}
                       value={password}
                       type="password" className="form-control" id="floatingPassword" placeholder="Password"/>
                <label htmlFor="floatingPassword">Password</label>
            </div>
            <div className="checkbox mb-3 mx-1">
                <label>
                    <input onChange={(e: React.ChangeEvent<HTMLInputElement>) => setRememberMe(e.target.checked)}
                           type="checkbox" value="remember-me"/> Remember me
                </label>
            </div>
            <button onClick={(e: React.MouseEvent) => submitForm(e)} className="w-100 btn btn-lg btn-primary">Sign in</button>
        </form>
    );
};

export default LoginForm;