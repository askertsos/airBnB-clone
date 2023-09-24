// userList.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function UserList() {

    const navigate = useNavigate();
	const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState([]);
    const [maxPage, setMaxPage] = useState();
    const [pageNum, setPageNum] = useState(0);
    const [pageSize, setPageSize] = useState(2);

	useEffect(() => {
        const reqBody = {
            "pageNo" : pageNum,
            "pageSize" : pageSize,
            "sort" : "ASC",
            "sortByColumn" : "id"
        };
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "post",
            body: JSON.stringify(reqBody)
		};
		fetch("https://localhost:8080/admin/user/list", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setUsers(response.Users.content);
            setMaxPage(response.Users.totalPages);
            setLoading(false);
        })
        .catch((message) => {
            navigate("/unauthorized/user");
            return;
            
        });
	}, [pageNum, pageSize, navigate]);

    const nextPage = () => {
        if (pageNum + 1 < maxPage) setPageNum(pageNum + 1);
    };

    const previousPage = () => {
        if (pageNum > 0) setPageNum(pageNum - 1);
    };

    const firstPage = () => {
        setPageNum(0);
    };

    const lastPage = () => {
        setPageNum(maxPage - 1);
    };

    const changePageSize = (newPageSize) => {
        setPageSize(newPageSize);
        setPageNum(0);
    };

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
            <div className="searchList-admin-bg ">
                <h2>Current page : {pageNum + 1}</h2>
                <button className="button" id="submit" type="button" onClick={() => nextPage()}>
                        Next Page
                </button>
                <button className="button" id="submit" type="button" onClick={() => previousPage()}>
                        Previous Page
                </button>
                {pageNum > 0 && 
                    <>
                        <button className="button" id="submit" type="button" onClick={() => firstPage()}>
                                First Page
                        </button>
                    </>
                }
                {pageNum < maxPage - 1 && 
                    <>
                        <button className="button" id="submit" type="button" onClick={() => lastPage()}>
                                Last Page
                        </button>
                    </>
                }
                <button className="button">
                    <label for="numPages" >
                        Users per page : 
                    </label>
                    <select name="numPages" id="pageSize" value={pageSize} onChange={(event) => changePageSize(event.target.value)}>
                        <option value= {1}> 1 </option>
                        <option value={2}> 2 </option>
                        <option value={5}> 5 </option>
                        <option value={10}> 10 </option>
                    </select>
                </button>
                <a href = 'https://localhost:3000/admin/home'>
                    <button className="button">
                        Homepage
                    </button>
                </a>
                <div>
                    <ul>
                        {users.map((data) => (
                            <p>
                                <a href={"https://localhost:3000/admin/user/" + data.id + "/details"}>
                                    <button className="user-list">
                                        <img className="admin-photo-field" src={require("../profile_photos/" + "default" + ".jpg")} alt="profilePic"/>
                                        <div className="admin-uname-field"> Username : {data.username} </div>
                                    </button>
                                </a>
                            </p>
                    ))}
                    </ul>
                </div>
            </div>
		</>
	);
}

export default UserList;