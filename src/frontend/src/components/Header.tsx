import React, {FC} from 'react';
import {NavLink} from "react-router-dom";
import {useAppSelector} from "../redux/hooks";

const Header: FC = () => {

    const user = useAppSelector(state => state.userReducer.user);

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark" aria-label="Eighth navbar example">
            <div className="container">
                <a className="navbar-brand">MyOLX</a>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarsExample07" aria-controls="navbarsExample07" aria-expanded="false"
                        aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarsExample07">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <NavLink to={"/"} className="nav-link">Home</NavLink>
                        </li>
                        <li className="nav-item">
                            {
                                user
                                    ?
                                    <NavLink to={"/profile/me"} className="nav-link">Profile</NavLink>
                                    :
                                    <NavLink to={"/login"} className="nav-link">Login</NavLink>
                            }
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    )
};

export default Header;