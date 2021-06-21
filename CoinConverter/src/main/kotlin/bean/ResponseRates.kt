package bean

data class ResponseRates(val sucess:Boolean, val base:String, val rates:Map<String, Double>)