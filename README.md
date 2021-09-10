##Commands

###Build docker image
`docker build --tag demo-hazelcast .`

###Create macvlan network
`docker network create -d macvlan -o parent=wlp0s20f3 --subnet 192.168.1.0/24 --gateway 192.168.1.1 --ip-range 192.168.1.192/27 --aux-address 'host=192.168.1.223' my-macvlan-net`

Note: created container cannot directly communicate with host network. run below commands to fix this. [source](https://blog.oddbit.com/post/2018-03-12-using-docker-macvlan-networks/)

1. `sudo ip link add mynet-macvlan link wlp0s20f3 type macvlan mode bridge`
2. `sudo ip addr add 192.168.1.223/32 dev mynet-macvlan`
3. `sudo ip link set mynet-macvlan up`
4. `sudo ip route add 192.168.1.192/27 dev mynet-macvlan`

###Run
`docker run --rm -itd -p 8082:8082 --ip=192.168.1.195 --network my-macvlan-net --name demo-hazelcast_1 demo-hazelcast:latest bash`
