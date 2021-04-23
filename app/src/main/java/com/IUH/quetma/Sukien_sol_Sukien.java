package com.IUH.quetma;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class Sukien_sol_Sukien extends Contract {
    public static final String BINARY = "60806040819052600080546001600160a01b03191633178082556001600160a01b0316917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0908290a3611de8806100576000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c80638da5cb5b116100665780638da5cb5b14610a455780638f32d59b14610a69578063cc25f26414610a85578063e540b8ff14610b3b578063e9aad2d714610b585761009e565b806337408ac8146100a357806347586a26146101615780635015b86114610427578063853b24be146106e6578063855780591461092c575b600080fd5b610111600480360360208110156100b957600080fd5b810190602081018135600160201b8111156100d357600080fd5b8201836020820111156100e557600080fd5b803590602001918460018302840111600160201b8311171561010657600080fd5b509092509050610c02565b60408051602080825283518183015283519192839290830191858101910280838360005b8381101561014d578181015183820152602001610135565b505050509050019250505060405180910390f35b610425600480360360e081101561017757600080fd5b813591602081013591810190606081016040820135600160201b81111561019d57600080fd5b8201836020820111156101af57600080fd5b803590602001918460018302840111600160201b831117156101d057600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561022257600080fd5b82018360208201111561023457600080fd5b803590602001918460018302840111600160201b8311171561025557600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b8111156102a757600080fd5b8201836020820111156102b957600080fd5b803590602001918460018302840111600160201b831117156102da57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561032c57600080fd5b82018360208201111561033e57600080fd5b803590602001918460018302840111600160201b8311171561035f57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b8111156103b157600080fd5b8201836020820111156103c357600080fd5b803590602001918460018302840111600160201b831117156103e457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610c7f945050505050565b005b610425600480360360c081101561043d57600080fd5b81359190810190604081016020820135600160201b81111561045e57600080fd5b82018360208201111561047057600080fd5b803590602001918460018302840111600160201b8311171561049157600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b8111156104e357600080fd5b8201836020820111156104f557600080fd5b803590602001918460018302840111600160201b8311171561051657600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561056857600080fd5b82018360208201111561057a57600080fd5b803590602001918460018302840111600160201b8311171561059b57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b8111156105ed57600080fd5b8201836020820111156105ff57600080fd5b803590602001918460018302840111600160201b8311171561062057600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561067257600080fd5b82018360208201111561068457600080fd5b803590602001918460018302840111600160201b831117156106a557600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506110eb945050505050565b610703600480360360208110156106fc57600080fd5b503561134b565b6040518089815260200180602001806020018060200180602001806020018881526020018715151515815260200186810386528d818151815260200191508051906020019080838360005b8381101561076657818101518382015260200161074e565b50505050905090810190601f1680156107935780820380516001836020036101000a031916815260200191505b5086810385528c5181528c516020918201918e019080838360005b838110156107c65781810151838201526020016107ae565b50505050905090810190601f1680156107f35780820380516001836020036101000a031916815260200191505b5086810384528b5181528b516020918201918d019080838360005b8381101561082657818101518382015260200161080e565b50505050905090810190601f1680156108535780820380516001836020036101000a031916815260200191505b5086810383528a5181528a516020918201918c019080838360005b8381101561088657818101518382015260200161086e565b50505050905090810190601f1680156108b35780820380516001836020036101000a031916815260200191505b5086810382528951815289516020918201918b019080838360005b838110156108e65781810151838201526020016108ce565b50505050905090810190601f1680156109135780820380516001836020036101000a031916815260200191505b509d505050505050505050505050505060405180910390f35b6109d06004803603602081101561094257600080fd5b810190602081018135600160201b81111561095c57600080fd5b82018360208201111561096e57600080fd5b803590602001918460018302840111600160201b8311171561098f57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061173a945050505050565b6040805160208082528351818301528351919283929083019185019080838360005b83811015610a0a5781810151838201526020016109f2565b50505050905090810190601f168015610a375780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610a4d6117e7565b604080516001600160a01b039092168252519081900360200190f35b610a716117f7565b604080519115158252519081900360200190f35b610b2960048036036020811015610a9b57600080fd5b810190602081018135600160201b811115610ab557600080fd5b820183602082011115610ac757600080fd5b803590602001918460018302840111600160201b83111715610ae857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550611808945050505050565b60408051918252519081900360200190f35b61070360048036036020811015610b5157600080fd5b5035611825565b610b7560048036036020811015610b6e57600080fd5b5035611b40565b6040518085815260200180602001848152602001838152602001828103825285818151815260200191508051906020019080838360005b83811015610bc4578181015183820152602001610bac565b50505050905090810190601f168015610bf15780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b60608060048484604051808383808284379190910194855250506040805193849003602090810185206001018054808302870183019093528286529350909150830182828015610c7157602002820191906000526020600020905b815481526020019060010190808311610c5d575b509398975050505050505050565b86610c8981611c15565b1515600114610c9757600080fd5b610c9f6117f7565b610ca857600080fd5b60005b600154811015610fab5760018181548110610cc257fe5b906000526020600020906008020160000154881415610d7d576001808281548110610ce957fe5b60009182526020822060089190910201600701805460ff1916921515929092179091555b600154811015610d775760018281548110610d2457fe5b9060005260206000209060080201600001548a1415610d6f57600060018381548110610d4c57fe5b60009182526020909120600890910201600701805460ff19169115159190911790555b600101610d0d565b50610fa3565b604080516101008101825289815260208082018a8152928201879052606082018990526080820188905260a082018690524260c0830152600160e083018190528054808201808355600083905284517fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf660089093029283019081559551805193969195949193610e34937fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf701929190910190611cd4565b5060408201518051610e50916002840191602090910190611cd4565b5060608201518051610e6c916003840191602090910190611cd4565b5060808201518051610e88916004840191602090910190611cd4565b5060a08201518051610ea4916005840191602090910190611cd4565b5060c0820151600682015560e0909101516007909101805460ff19169115159190911790555060405185516003918791819060208401908083835b60208310610efe5780518252601f199092019160209182019101610edf565b51815160209384036101000a600019018019909216911617905292019485525060405193849003019092208054600101905550600090505b600154811015610fa05760018381548110610f4d57fe5b9060005260206000209060080201600001548b1415610f9857600060018481548110610f7557fe5b60009182526020909120600890910201600701805460ff19169115159190911790555b600101610f36565b50505b600101610cab565b506040805160808101825289815260208082018581524293830193909352606082018a90526002805460018181018084556000938452855160049093027f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5ace810193845596518051949792969195919461104b937f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5acf01929190910190611cd4565b50604082015181600201556060820151816003015550500390506004836040518082805190602001908083835b602083106110975780518252601f199092019160209182019101611078565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381019093206001908101805491820181556000908152939093209092019290925550505050505050505050565b856110f581611c85565b15156001141561110457600080fd5b61110c6117f7565b61111557600080fd5b60408051610100810182528881526020808201898152928201869052606082018890526080820187905260a082018590524260c0830152600160e0830181905280548082018083556000839052845160089092027fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf681019283559551805193968796929592946111cd937fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf79092019290910190611cd4565b50604082015180516111e9916002840191602090910190611cd4565b5060608201518051611205916003840191602090910190611cd4565b5060808201518051611221916004840191602090910190611cd4565b5060a0820151805161123d916005840191602090910190611cd4565b5060c0820151600682015560e0909101516007909101805460ff1916911515919091179055505060405184516003918691819060208401908083835b602083106112985780518252601f199092019160209182019101611279565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381018420805460010190558651879460049450859350918291908401908083835b602083106112ff5780518252601f1990920191602091820191016112e0565b51815160209384036101000a600019018019909216911617905292019485525060405193849003810190932084516113409591949190910192509050611cd4565b505050505050505050565b60006060808080808580805b60015481101561172d576001818154811061136e57fe5b9060005260206000209060080201600001548a14156117255761138f611d52565b6001828154811061139c57fe5b90600052602060002090600802016040518061010001604052908160008201548152602001600182018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156114595780601f1061142e57610100808354040283529160200191611459565b820191906000526020600020905b81548152906001019060200180831161143c57829003601f168201915b5050509183525050600282810180546040805160206001841615610100026000190190931694909404601f810183900483028501830190915280845293810193908301828280156114eb5780601f106114c0576101008083540402835291602001916114eb565b820191906000526020600020905b8154815290600101906020018083116114ce57829003601f168201915b505050918352505060038201805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815293820193929183018282801561157f5780601f106115545761010080835404028352916020019161157f565b820191906000526020600020905b81548152906001019060200180831161156257829003601f168201915b505050918352505060048201805460408051602060026001851615610100026000190190941693909304601f81018490048402820184019092528181529382019392918301828280156116135780601f106115e857610100808354040283529160200191611613565b820191906000526020600020905b8154815290600101906020018083116115f657829003601f168201915b505050918352505060058201805460408051602060026001851615610100026000190190941693909304601f81018490048402820184019092528181529382019392918301828280156116a75780601f1061167c576101008083540402835291602001916116a7565b820191906000526020600020905b81548152906001019060200180831161168a57829003601f168201915b50505050508152602001600682015481526020016007820160009054906101000a900460ff1615151515815250509050806000015181602001518260400151836060015184608001518560a001518660c001518760e0015186965085955084945083935082925099509950995099509950995099509950505061172f565b600101611357565b505b919395975091939597565b805160208183018101805160048252928201938201939093209190925280546040805160026001841615610100026000190190931692909204601f81018590048502830185019091528082529192909183918301828280156117dd5780601f106117b2576101008083540402835291602001916117dd565b820191906000526020600020905b8154815290600101906020018083116117c057829003601f168201915b5050505050905081565b6000546001600160a01b03165b90565b6000546001600160a01b0316331490565b805160208183018101805160038252928201919093012091525481565b6001818154811061183257fe5b9060005260206000209060080201600091509050806000015490806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156118e45780601f106118b9576101008083540402835291602001916118e4565b820191906000526020600020905b8154815290600101906020018083116118c757829003601f168201915b50505060028085018054604080516020601f60001961010060018716150201909416959095049283018590048502810185019091528181529596959450909250908301828280156119765780601f1061194b57610100808354040283529160200191611976565b820191906000526020600020905b81548152906001019060200180831161195957829003601f168201915b5050505060038301805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152949594935090830182828015611a065780601f106119db57610100808354040283529160200191611a06565b820191906000526020600020905b8154815290600101906020018083116119e957829003601f168201915b5050505060048301805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152949594935090830182828015611a965780601f10611a6b57610100808354040283529160200191611a96565b820191906000526020600020905b815481529060010190602001808311611a7957829003601f168201915b5050505060058301805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152949594935090830182828015611b265780601f10611afb57610100808354040283529160200191611b26565b820191906000526020600020905b815481529060010190602001808311611b0957829003601f168201915b50505050600683015460079093015491929160ff16905088565b60028181548110611b4d57fe5b9060005260206000209060040201600091509050806000015490806001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015611bff5780601f10611bd457610100808354040283529160200191611bff565b820191906000526020600020905b815481529060010190602001808311611be257829003601f168201915b5050505050908060020154908060030154905084565b6000805b600154811015611c7e578260018281548110611c3157fe5b9060005260206000209060080201600001541415611c765760018181548110611c5657fe5b600091825260209091206007600890920201015460ff169150611c809050565b600101611c19565b505b919050565b6000805b600154811015611ccb578260018281548110611ca157fe5b9060005260206000209060080201600001541415611cc3576001915050611c80565b600101611c89565b50600092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611d1557805160ff1916838001178555611d42565b82800160010185558215611d42579182015b82811115611d42578251825591602001919060010190611d27565b50611d4e929150611d99565b5090565b604051806101000160405280600081526020016060815260200160608152602001606081526020016060815260200160608152602001600081526020016000151581525090565b6117f491905b80821115611d4e5760008155600101611d9f56fea265627a7a72315820aba79d02478292c1b1ad608410f546b9a08cc8175b35b6ddde345ec3735c5ebb64736f6c63430005110032";

    public static final String FUNC_VES = "Ves";

    public static final String FUNC_CREATEVE = "createVe";

    public static final String FUNC_GIAODICH = "giaoDich";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_LICHSU = "lichSu";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_SEARVE = "searVe";

    public static final String FUNC_SUKIENMAPPING = "sukienMapping";

    public static final String FUNC_TIMLINHSU = "timLinhSu";

    public static final String FUNC_VEMAPPING = "veMapping";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected Sukien_sol_Sukien(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Sukien_sol_Sukien(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Sukien_sol_Sukien(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Sukien_sol_Sukien(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>> Ves(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>>(function,
                new Callable<Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>>() {
                    @Override
                    public Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (String) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (Boolean) results.get(7).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> createVe(BigInteger _mssv, String _nguoitao, String _ho, String _ten, String _masukien, String _mave) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_mssv), 
                new org.web3j.abi.datatypes.Utf8String(_nguoitao), 
                new org.web3j.abi.datatypes.Utf8String(_ho), 
                new org.web3j.abi.datatypes.Utf8String(_ten), 
                new org.web3j.abi.datatypes.Utf8String(_masukien), 
                new org.web3j.abi.datatypes.Utf8String(_mave)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> giaoDich(BigInteger _mssv1, BigInteger _mssv2, String _nguoitao, String _ho, String _ten, String _masukien, String _mave) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GIAODICH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_mssv1), 
                new org.web3j.abi.datatypes.generated.Uint256(_mssv2), 
                new org.web3j.abi.datatypes.Utf8String(_nguoitao), 
                new org.web3j.abi.datatypes.Utf8String(_ho), 
                new org.web3j.abi.datatypes.Utf8String(_ten), 
                new org.web3j.abi.datatypes.Utf8String(_masukien), 
                new org.web3j.abi.datatypes.Utf8String(_mave)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> isOwner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Tuple4<BigInteger, String, BigInteger, BigInteger>> lichSu(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_LICHSU, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple4<BigInteger, String, BigInteger, BigInteger>>(function,
                new Callable<Tuple4<BigInteger, String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple4<BigInteger, String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, String, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>> searVe(BigInteger _mssv) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SEARVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_mssv)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>>(function,
                new Callable<Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>>() {
                    @Override
                    public Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (String) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (Boolean) results.get(7).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> sukienMapping(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SUKIENMAPPING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<List> timLinhSu(String _mave) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TIMLINHSU, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_mave)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> veMapping(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VEMAPPING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Sukien_sol_Sukien load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sukien_sol_Sukien(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Sukien_sol_Sukien load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sukien_sol_Sukien(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Sukien_sol_Sukien load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Sukien_sol_Sukien(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Sukien_sol_Sukien load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Sukien_sol_Sukien(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Sukien_sol_Sukien> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Sukien_sol_Sukien.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Sukien_sol_Sukien> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Sukien_sol_Sukien.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Sukien_sol_Sukien> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Sukien_sol_Sukien.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Sukien_sol_Sukien> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Sukien_sol_Sukien.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
