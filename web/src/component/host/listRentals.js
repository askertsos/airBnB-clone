// listRentals.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function ListRentals() {

    const navigate = useNavigate();
	const [loading, setLoading] = useState(true);
    const [rentals, setRentals] = useState([]);
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
		fetch("https://localhost:8080/host/rental/list", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setRentals(response.Rentals.content);
            setMaxPage(response.Rentals.totalPages);
            setLoading(false);
        })
        .catch((message) => {
            console.log(message);
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
            <div className="rentalList-host-bg">
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
                            Rentals per page 
                        </label>
                        <select name="numPages" id="pageSize" value={pageSize} onChange={(event) => changePageSize(event.target.value)}>
                            <option value= {1}> 1 </option>
                            <option value={2}> 2 </option>
                            <option value={5}> 5 </option>
                            <option value={10}> 10 </option>
                        </select>
                    </button>
                    <a href = 'https://localhost:3000/host/hostHome'>
                        <button className="button" id="submit" type="button">
                                HomePage
                        </button>
                    </a>
                    <div>
                    <ul>
                        {rentals.map((data) => (
                            <p>
                                <a href={"https://localhost:3000/host/rental/" + data.id + "/details"}>
                                    <button className="rental host-rental-list">
                                        <img className="rentalPic" src={require("../profile_photos/" + "default" + ".jpg")} alt="profilePic"/>
                                        <p className="rental-field1"> Title : {data.title} </p>
                                        <p className="rental-field1"> Charge per person : {data.chargePerPerson} </p>
                                        <p className="rental-field1"> Type : {data.type} </p>
                                        <p className="rental-field2"> Number of beds : {data.beds} </p>
                                        <p className="rental-field2"> Number of reviews : {data.reviews.length} </p>
                                        <p className="rental-field2"> Rating : {data.rating} </p>
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

export default ListRentals;