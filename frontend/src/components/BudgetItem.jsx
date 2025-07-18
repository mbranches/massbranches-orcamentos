function BudgetItem({ children }) {
    return (
        <tr className='border-b border-b-gray-200 sticky bottom-0'>
            {children}    
        </tr>
    );
}

export default BudgetItem;