import React, {FC, useEffect, useState} from "react";
import {useAppSelector} from "../redux/hooks";
import ProductsHOC from "./ProductsHOC";
import {NavLink, useParams} from "react-router-dom";
import {axiosInstance} from "../axios";
import {IUser} from "../models/IUser";

const Profile: FC = () => {

    const [user, setUser] = useState<IUser | null>();
    const [isLoading, setIsLoading] = useState<boolean>();
    const [error, setError] = useState<string>();

    const {id} = useParams();
    const userData = useAppSelector((state) => state.userReducer);

    useEffect(() => {
        if (!id) {
            setUser(userData.user);
            setIsLoading(userData.isLoading);
            setError(userData.error);
        } else {
            setIsLoading(true);
            axiosInstance.get<IUser>(`/users/id/${id}`)
                .then(response => {
                    setUser(response.data);
                    setIsLoading(false);
                })
                .catch(response => {
                    setIsLoading(false);
                    setError("Error")
                })
        }
    }, [])

    if (isLoading || !user) return <div>Loading...</div>
    if (error) return <div>{error}</div>
    return (
        <div className="my-4 px-5 container">
            <div className="row w-25 bg-white rounded m-auto p-2 mb-3 text-center">
                <h5>{user.name}</h5>
                <div className="text-muted small">
                    <p className="m-0">{user.email}</p>
                    <p className="m-0">{user.location}</p>
                </div>
                {
                    !id &&
                    <div className="d-flex flex-column mt-4">
                        <NavLink to={`/products/add`} className="btn btn-primary btn-sm" type="button">
                            Add new product
                        </NavLink>
                        <button className="btn btn-outline-primary btn-sm mt-2" type="button">
                            Edit profile
                        </button>
                    </div>
                }
            </div>
            <div className="row w-25 bg-white rounded m-auto p-2 mb-3">
                <h5 className="m-0 text-center">Your products:</h5>
            </div>
            <ProductsHOC products={user.products}/>
        </div>
    );
};

export default Profile;