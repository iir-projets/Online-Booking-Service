function Comment(props){

    return(
        <div className="p-4 border-2 h-24 rounded-2xl overflow-y-auto">
        <h1>User: {props.user}</h1>
        <p className="text-sm">Comment : {props.comment}</p>
        </div>
    )
}

export default Comment