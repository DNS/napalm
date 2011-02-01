from freshen import *
import httplib2, os, re, json
from urllib import urlencode

@Given("base URL '(.+)'")
def given_base_url(url):
    scc.base_url = url;
    
@When(r"send a (GET|DELETE) to '(.+)'")
def when_get_url(method,url):
    full_url = get_full_url(url)
    h = httplib2.Http()
    scc.response, scc.content = h.request(full_url, method)
    
@Then(r"expect HTTP (\d+)\s*")
def then_expect_http_code(code):
    assert_equals(int(code),int(scc.response.status), msg="%s != %s\n%s\n%s" % (code,scc.response.status,scc.response,scc.content))    
    
@Then("expect text equivalent to '(.+)'\s*")
def then_expect_text(text):
    assert_equals(text,scc.content, msg="'%s' != '%s'" % (text,scc.content))

@Then("expect JSON equivalent to '(.+)'\s*")
def then_expect_json(content):
    json_obj = json.loads(content)
    json_sorted = json.dumps(json_obj,sort_keys=True,indent=4)
    last_obj = json.loads(scc.content)
    last_sorted = json.dumps(last_obj,sort_keys=True,indent=4)
    assert_equals(json_sorted,last_sorted,"Expected JSON\n%s\n*** actual ****\n%s" % (json_sorted,last_sorted))
    
def get_full_url(url):
    return "%s%s" % (scc.base_url,url)    